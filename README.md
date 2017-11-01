

- login

    ```
    url : http://www.wanandroid.com/user/login

    http: POST

    data: username=thatnight&password=xxxxxxxx

    return:{
           	"errorCode": 0,
           	"errorMsg": null,
           	"data": {
           		"id": 624,
           		"username": "thatnight",
           		"password": "xxxxxxxxx",
           		"icon": null,
           		"type": 0,
           		"collectIds": []
           	}
           }
    ```
    
- register

    ```
    url : http://www.wanandroid.com/user/register

    http: POST

    data: username=thatnight1&password=xxxxxxx&repassword=xxxxxx

    return:{
           	"errorCode": 0,
           	"errorMsg": null,
           	"data": {
           		"id": 624,
           		"username": "thatnight",
           		"password": "xxxxxxxxx",
           		"icon": null,
           		"type": 0,
           		"collectIds": []
           	}
           }
    ```


- collect

    ```
    url : http://www.wanandroid.com/lg/collect/1484/json

    http: POST

    return:{
           	"errorCode": 0,
           	"errorMsg": null,
           	"data": null
           }
    ```