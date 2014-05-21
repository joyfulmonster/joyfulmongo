<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <h2>KongChePei webRest Interface</h2>

<pre>

服务地址 
http://511golf.com:8090/kcpserver/webapi/1
http://kongchepei.duapp.com/webapi/1

1.管车宝web用户

1.1. 创建管车宝web用户

GET http://localhost:8080/kcpserver/webapi/1/web-dispatcher/new?phoneNo=123455

POST http://localhost:8080/kcpserver/webapi/1/web-dispatcher 
Parameter: {"phoneNo":"18800000000"}
Header: Content-Type application/json

Return on Success: callback({"objectId":"MGGjz92L6r","sessionToken":"7eaet0dakzucm9uoigln2475i","status":200})
Return on Failure: 
                   400: 输入电话号码参数是空 
                   404: 电话号码不是管车宝用户
                   500: 服务器内部错误                   

1.2. 登录管车宝web用户登录

GET  http://localhost:8080/kcpserver/webapi/1/web-dispatcher?phoneNo=18800000000&verifycode=89898

phoneNo是 1 中输入的官车宝电话
verifycode 是用户输入的验证码. 89898是万能码用以测试.

Return on Success: callback({"objectId":"MGGjz92L6r","sessionToken":"7eaet0dakzucm9uoigln2475i","status":200})
Return on Failure: callback({"error":"Invalid phoneNo and verifyCode combination","status":401})
                   400: 输入电话号码或密码参数是空
                   401: 密码错误。
                   500: 服务器内部错误                   

注意这里的 "sessionToken", 程序用这个住这个sessionToken进行下面的操作. sessionToken的过期时间为一小时. 

1.3. 请求访问一个管车宝

GET http://localhost:8080/kcpserver/webapi/1/web-dispatcher/getgroup?sessionToken=7eaet0dakzucm9uoigln2475i&phoneNo=18700000000

POST http://localhost:8080/kcpserver/webapi/1/web-dispatcher/group?sessionToken=7eaet0dakzucm9uoigln2475i

Parameter:  {"phoneNo":"18700000000"}
 
Return on Success: callback({"status":200})
Return on Failure: 
                   202: 用户曾经请求过同一物流。这次请求也接受并处理了。
                   412: sessionToken不对或是过期了
                   404: 电话号码不是管车宝用户 callback({"error":"管车宝18500000000尚未注册.","status":404})
                   500: 服务器内部错误                                      

1.4 验证访问一个管车宝的请求

GET http://localhost:8080/kcpserver/webapi/1/web-dispatcher/getgroup/verify?phoneNo=13366292989&verifycode=Ypv2ytV9&sessionToken=g8y82a2fcvm17sq1gm92j6z3q
Return on Success: callback({"status":200})
Return on Failure: 
                   412: sessionToken不对或是过期了
                   400: 用户尚没有发送管车宝请求
                   401: 密码不对 
                   500: 服务器内部错误                                      


1.5. 访问可以监控的物流

GET http://localhost:8080/kcpserver/webapi/1/web-dispatcher/groups?sessionToken=uav9pqy7pstq0yhzm343ig0hz

Return on Success: callback([{"@xsi:type":"jsonLogisticGroupBean","objectId":"wcOJmamILm","address":"","bizPermitUrl":"","description":"","groupName":"清空名称","serviceAgreement":"","shippingPermitUrl":""},{"@xsi:type":"jsonLogisticGroupBean","objectId":"QFApPTOP1f","address":"杨幂老师傅","bizPermitUrl":"","description":"","groupName":"杨老师傅","serviceAgreement":"","shippingPermitUrl":""},{"@xsi:type":"jsonLogisticGroupBean","objectId":"e79Ck53LUg","address":"上海徐汇区","bizPermitUrl":"null180","description":"位于上海出差了","groupName":"6666管车宝","serviceAgreement":"","shippingPermitUrl":"null190"}])
Return on Failure:  
                   412: sessionToken不对或是过期了
                   500: 服务器内部错误                                                                            

记住这个 JSON数组里的 "objectId" 和  "groupName". 

1.6. 查看物流的车辆 

GET http://localhost:8080/kcpserver/webapi/1/web-dispatcher/group/e79Ck53LUg/trucks?sessionToken=7eaet0dakzucm9uoigln2475i

注意:  user/group/{objectId}/trucks objectId是从4得来的.

Return on Success: callback([{"@xsi:type":"jfUserDriver","companyName":"18964651040","distanceInKiloMeter":0.0,"endCity":"北京","latitude":31.159232,"longitude":121.439375,"mobileNo":"18964651040","photoUrls":{"entry":[]},"realname":"张星宇司机宝","revision":"","securityEmail":"","securityWechat":"","startCity":"上海","userId":"q2Wo7i4sEI","username":"SD18964651040","driverIdCardPhotoUrl":"q2Wo7i4sEI200","driverLicensePhotoUrl":"q2Wo7i4sEI210","driverPermitPhotoUrl":"q2Wo7i4sEI220","idCardId":"","truckCapacity":5,"truckLength":"9.6米","truckPhotoUrl":"q2Wo7i4sEI130","truckPlate":"沪H51040","truckState":"Emtpy","truckType":"高栏"}])
Return on Failure: 
                   412: sessionToken不对或是过期了
                   500: 服务器内部错误                                                                            

1.7. 查看物流的运单  

GET http://localhost:8080/kcpserver/webapi/1/web-dispatcher/group/e79Ck53LUg/waybills?sessionToken=7eaet0dakzucm9uoigln2475i

Return on Success: 
Return on Failure: 

1.8 查看自己的物流

GET http://localhost:8080/kcpserver/webapi/1/web-dispatcher/waybills?sessionToken=g8y82a2fcvm17sq1gm92j6z3q

Return on Success: callback([{"xsi:type":"jfWayBillBean","createdAtStr":"null","lastUpdateAtStr":"null","confirmationState":"Accepted","departAddress":"","departCity":"北京","description":"西洋参","destinationCity":"北京","driverInfo":{"userObjectId":"fYF5CZgXBd","truckPlateString":"京,0,A,0,16002"},"price":30,"shipperInfo":{"userObjectId":"g5gHVUZHMr"},"volume":10,"wayBillId":"30121375956568881","weight":20},{"xsi:type":"jfWayBillBean","createdAtStr":"null","lastUpdateAtStr":"null","confirmationState":"Accepted","departAddress":"","departCity":"北京","description":"西洋参","destinationCity":"北京","driverInfo":{"userObjectId":"fYF5CZgXBd","truckPlateString":"京,0,A,0,16002"},"price":30,"shipperInfo":{"userObjectId":"g5gHVUZHMr"},"volume":10,"wayBillId":"30121375956568881","weight":20},{"xsi:type":"jfWayBillBean","createdAtStr":"null","lastUpdateAtStr":"null","confirmationState":"Accepted","departAddress":"","departCity":"北京","description":"","destinationCity":"北京","driverInfo":{"userObjectId":"fYF5CZgXBd","truckPlateString":"京,0,A,0,16002"},"price":67,"shipperInfo":{"userObjectId":"g5gHVUZHMr"},"volume":23,"wayBillId":"30121375956619763","weight":45},{"xsi:type":"jfWayBillBean","createdAtStr":"null","lastUpdateAtStr":"null","confirmationState":"Accepted","departAddress":"","departCity":"北京","description":"","destinationCity":"北京","driverInfo":{"userObjectId":"fYF5CZgXBd","truckPlateString":"京,0,A,0,16002"},"price":67,"shipperInfo":{"userObjectId":"g5gHVUZHMr"},"volume":23,"wayBillId":"30121375956619763","weight":45}])
Return on Failure: 
                   412: sessionToken不对或是过期了
                   500: 服务器内部错误                                                                            

2.货主宝web用户

2.1. 创建货主宝web用户

GET  http://localhost:8080/kcpserver/webapi/1/web-shipper/new?phoneNo=74447544587

OR:

POST http://localhost:8080/kcpserver/webapi/1/web-shipper
 
Parameter: {"phoneNo":"13951913928"}
Header: Content-Type application/json

Return on Success: callback({"objectId":"Vaaud3VOmY","sessionToken":"y4sa2u8zge9bu5bevndbrcyt3","status":200})
Return on Failure:  {"error":"The phone number 13951913929 is not a registered shipper app user.","status":400}

2.2. 登录管车宝web用户登录

GET  http://localhost:8080/kcpserver/webapi/1/web-shipper?phoneNo=74447544587&verifycode=89898

phoneNo是 1 中输入的货主宝电话
verifycode 是用户输入的验证码. 89898是万能码用以测试.

Return on Success: HTTP status is 200  BODY: callback({"objectId":"7ARwgzuuPy","sessionToken":"mjqozbue85j5rw7h4cqif69ti","status":200})
Return on Failure:  {"error":"Invalid phoneNo and verifyCode combination","status":401}

注意这里的 "sessionToken", 程序用这个住这个sessionToken进行下面的操作. sessionToken的过期时间为一小时. 

2.3. 请求访问一个管车宝

POST http://localhost:8080/kcpserver/webapi/1/web-shipper/group?sessionToken=mjqozbue85j5rw7h4cqif69ti

Parameter:  {"phoneNo":"16100000000"}
 
Return on Success: callback({"status":200})
Return on Failure: callback({"error":"管车宝18500000000尚未注册.","status":404})

2.4. 访问可以监控的物流

GET http://localhost:8080/kcpserver/webapi/1/web-shipper/groups?sessionToken=mjqozbue85j5rw7h4cqif69ti

Return on Success: callback([{"xsi:type":"jsonLogisticGroupBean","objectId":"usmfcwTcrr","address":"小关东里","bizPermitUrl":"null180","description":"5789","groupName":"物流测试运单","serviceAgreement":"","shippingPermitUrl":"null190"}])
Return on Failure: callback({"error":"Your login session has expired due to inactivity, please relogin.","status":401}) 

记住这个 JSON数组里的 "objectId" 和  "groupName". 

2.5. 查看物流的车辆 

GET http://localhost:8080/kcpserver/webapi/1/web-shipper/group/usmfcwTcrr/trucks?sessionToken=mjqozbue85j5rw7h4cqif69ti

注意:  user/group/{objectId}/trucks objectId是从4得来的.

Return on Success: callback([{"@xsi:type":"jfUserDriver","companyName":"18964651040","distanceInKiloMeter":0.0,"endCity":"北京","latitude":31.159232,"longitude":121.439375,"mobileNo":"18964651040","photoUrls":{"entry":[]},"realname":"张星宇司机宝","revision":"","securityEmail":"","securityWechat":"","startCity":"上海","userId":"q2Wo7i4sEI","username":"SD18964651040","driverIdCardPhotoUrl":"q2Wo7i4sEI200","driverLicensePhotoUrl":"q2Wo7i4sEI210","driverPermitPhotoUrl":"q2Wo7i4sEI220","idCardId":"","truckCapacity":5,"truckLength":"9.6米","truckPhotoUrl":"q2Wo7i4sEI130","truckPlate":"沪H51040","truckState":"Emtpy","truckType":"高栏"}])
Return on Failure: callback({"error":"Your login session has expired due to inactivity, please relogin.","status":401})

2.6. 查看附近的车辆

GET: http://localhost:8080/kcpserver/webapi/1/web-shipper/trucks?sessionToken=y4sa2u8zge9bu5bevndbrcyt3&limit=40&skip=0

返回40辆车。

GET: http://localhost:8080/kcpserver/webapi/1/web-shipper/trucks?sessionToken=y4sa2u8zge9bu5bevndbrcyt3&limit=40&skip=40

返回下40辆车。

Return on Success: callback([{"xsi:type":"jfUserDriver","companyName":"15300000005","distanceInKiloMeter":0.0,"endCity":"北京","latitude":39.977576,"longitude":116.45502,"mobileNo":"15300000005","photoUrls":{"entry":[]},"startCity":"北京","userId":"fL5dSYCu21","username":"SD15300000005","truckLength":"未选","truckPlate":"京A15305","truckState":"Emtpy","truckType":"未选"},{"xsi:type":"jfUserDriver","companyName":"18800000000","distanceInKiloMeter":0.0,"endCity":"待定","latitude":32.042482,"longitude":118.76245,"mobileNo":"18800000000","photoUrls":{"entry":[]},"realname":"杨老师","startCity":"南京","userId":"PKo67wQqff","username":"SD18800000000","driverIdCardPhotoUrl":"PKo。。。。。。
Return on Failure: callback({"error":"Your login session has expired due to inactivity, please relogin.","status":401})

2.7 查看为我运货的车辆

GET：http://localhost:8080/kcpserver/webapi/1/web-shipper/mytrucks?sessionToken=y4sa2u8zge9bu5bevndbrcyt3&limit=40&skip=0

返回40辆车。

GET: http://localhost:8080/kcpserver/webapi/1/web-shipper/mytrucks?sessionToken=y4sa2u8zge9bu5bevndbrcyt3&limit=40&skip=40

返回下40辆车。

Return on Success: callback([{"xsi:type":"jfUserDriver","companyName":"15300000005","distanceInKiloMeter":0.0,"endCity":"北京","latitude":39.977576,"longitude":116.45502,"mobileNo":"15300000005","photoUrls":{"entry":[]},"startCity":"北京","userId":"fL5dSYCu21","username":"SD15300000005","truckLength":"未选","truckPlate":"京A15305","truckState":"Emtpy","truckType":"未选"},{"xsi:type":"jfUserDriver","companyName":"18800000000","distanceInKiloMeter":0.0,"endCity":"待定","latitude":32.042482,"longitude":118.76245,"mobileNo":"18800000000","photoUrls":{"entry":[]},"realname":"杨老师","startCity":"南京","userId":"PKo67wQqff","username":"SD18800000000","driverIdCardPhotoUrl":"PKo。。。。。。
Return on Failure: callback({"error":"Your login session has expired due to inactivity, please relogin.","status":401})


</pre>

</body>
</html>
