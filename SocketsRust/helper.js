
function  StringCounter ( input){
    filtered=new Set(input);
    let check=filtered.has(' ');
    return check ? filtered.size-1 :filtered.size;
}


input = "aaa bbb cc ttt rt";
console.log(StringCounter(input));