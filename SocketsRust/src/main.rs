extern crate ws;

use std::cell::Cell;
use std::rc::Rc;

use ws::{
    listen, CloseCode, Error, Handler, Handshake, Message, Request, Response, Result, Sender,
};


static INDEX_HTML: &'static [u8] = br#"
<!doctype html>
<html>

<head>
  <title>Websocket Rust chat</title>
  <!-- <link rel='stylesheet' href='/main.css'> -->
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font: 13px Helvetica, Arial;
    }

    form {
      background: #000;
      padding: 3px;
      position: fixed;
      bottom: 0;
      width: 100%;
    }

    form input {
      border: 0;
      padding: 10px;
      width: 90%;
      margin-right: .5%;
    }

    form button {
      width: 9%;
      background: rgb(130, 224, 255);
      border: none;
      padding: 10px;
    }

    #messages {
      list-style-type: none;
      margin: 0;
      padding: 0;
    }

    #messages li {
      padding: 5px 10px;
    }

    #messages li:nth-child(odd) {
      background: #eee;
    }

  </style>
</head>

<body>
  <ul id='messages'></ul>
  <form id='form'>
    <input type='text' id='msg' autocomplete='off' >
    <button>Send</button>
  </form>
</body>

<script>


  const socket = new WebSocket('ws://' + window.location.host + '/ws');

  const form = document.getElementById('form');

  function reverseString (str) {
  return (str === '') ? '' : reverseString(str.substr(1)) + str.charAt(0);
  }

  function  stringCounter ( input){
    filtered=new Set(input);
    let check=filtered.has(' ');
    return check ? filtered.size-1 :filtered.size;
}



  form.addEventListener('submit', function (event) {
    event.preventDefault();
    const input = document.getElementById('msg');
    if (input.value == '!exit') {
      const messages = document.getElementById('messages');
      const li = document.createElement('li');
      li.append("You leave the server.")
      messages.append(li);
      socket.close();
      input.value = '';
    }
    if (input.value === '!clear') {
      const messages = document.getElementById('messages');
      while (messages.firstChild) {
        messages.removeChild(messages.firstChild);
      };
      input.value = '';
      return;
    }
    if (input.value.slice(0, 2) === '!#') {
      input.value = '';
      return;
    }
    socket.send(stringCounter(input.value));
    input.value = '';
  });

  socket.addEventListener('open', function (event) {
    socket.send(stringCounter(input.value));
  });

  socket.onmessage = function (event) {
    console.log(`${event.data} from ${event.origin}`);
    const messages = document.getElementById('messages');
    const li = document.createElement('li');
    li.append(event.data)
    messages.append(li);
  };

  socket.onclose = function(event) {
    const messages = document.getElementById('messages');
    const li = document.createElement('li');
    li.append(`You don't have connection to the server. It won't work.`);
    messages.append(li);
    console.log('Chat stopped');
  };

</script>

</html>
    "#;

struct Server {
    out: Sender,
    count: Rc<Cell<u32>>,
}

impl Handler for Server {
    fn on_request(&mut self, req: &Request) -> Result<(Response)> {
        match req.resource() {
          
            "/ws" => {
                let resp = Response::from_request(req);
                resp
            }

            "/" => Ok(Response::new(200, "OK", INDEX_HTML.to_vec())), 

            _ => Ok(Response::new(404, "Not Found", b"404 - Not Found".to_vec())), 
        }
    }

    fn on_open(&mut self, _: Handshake) -> Result<()> {
        Ok(self.count.set(self.count.get() + 1))
    }

    fn on_message(&mut self, msg: Message) -> Result<()> {
        let number_of_connection = self.count.get();
        println!(
            "The number of live connections is {}\n",
            &number_of_connection
        );

        let msg_from_user = msg.as_text().unwrap();

        println!("The message from the client is {:?}", msg_from_user);

     
        self.out.broadcast(msg)
    }


    fn on_close(&mut self, code: CloseCode, reason: &str) {
        match code {
            CloseCode::Normal => println!("The client is done with the connection."),
            CloseCode::Away => println!("The client is leaving the site."),
            CloseCode::Abnormal => {
                println!("Closing handshake failed! Unable to obtain closing status from client.")
            }
            _ => println!("The client encountered an error: {}", reason),
        }
        self.count.set(self.count.get() - 1)
    }

    fn on_error(&mut self, err: Error) {
        println!("The server encountered an error: {:?}", err);
    }
}

fn main() {
    println!("Web Socket Server is ready at //127.0.0.1:7777");
    println!("Server is ready at http://127.0.0.1:7777/");
    let count = Rc::new(Cell::new(0));
    listen("127.0.0.1:7777", |out| Server {
        out: out,
        count: count.clone(),
    })
    .unwrap()
}

