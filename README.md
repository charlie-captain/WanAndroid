
- frame
    - MVP
    - [SwipeBacklayout](https://github.com/ikew0ng/SwipeBackLayout) --右滑返回
    - [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout) --recyclerview下拉刷新上拉加载
    - [AnimButton](https://github.com/thatnight/AnimButton) --登陆按钮
    - Gson --解析Json
    - 

- api
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
    - article
    
        ```
        url: http://www.wanandroid.com/article/list/0/json
        
        return:{"errorCode":0,"errorMsg":null,"data":{"datas":[{"id":1485,"title":"Android中图片压缩分析（上）","chapterId":86,"chapterName":"图片处理","envelopePic":null,"link":"https://mp.weixin.qq.com/s/QZ-XTsO7WnNvpnbr3DWQmg","author":"QQ音乐技术团队","origin":null,"publishTime":1509611122000,"zan":0,"desc":null,"visible":0,"niceDate":"2小时前","courseId":13,"collect":false}
        ],"offset":0,"size":20,"total":771,"over":false}}
       
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
        
    - uncollect
    
        ```
        url :  http://www.wanandroid.com/lg/uncollect_originId/1485/json
    
        http: POST
    
        return:{
                "errorCode": 0,
                "errorMsg": null,
                "data": null
               }
        ```
        
    - collect articles
    
        ```
        url :  http://www.wanandroid.com/lg/collect/list/0/json
    
        http: POST
    
        return:{
                "errorCode": 0,
                "errorMsg": null,
                "data": null
               }
        ```   
        
        - uncollect 
        
            ```
            url :  http://www.wanandroid.com/lg/uncollect/407/json
                
            http: POST
            
            params: originId=399
                
            return:{
                    "errorCode": 0,
                    "errorMsg": null,
                    "data": null
                   }
            ```
            