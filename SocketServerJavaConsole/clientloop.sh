@echo off
:loop
java -cp..\target\classes\com\company\phone\Phone.jar;. \target\classes\com\company\client\Client
goto loop
