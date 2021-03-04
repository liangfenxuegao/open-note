/**
 * 必须遵守ECMAScript5.1语法标准。
 * 本脚本用于向open-note模块查询数据库中的地区信息，并存入数组中。
 * */

//自定义的Ajax方法，通过原生Javascript代码实现。参考文章：https://www.jianshu.com/p/ea064da40e25 。
var Ajax = {
    get: function(url,callback){
        var xhr = new XMLHttpRequest();//XMLHttpRequest对象用于在后台与服务器交换数据
        xhr.open('GET',url,false);//设置发起参数
        //当状态改变时触发函数
        xhr.onreadystatechange = function(){
            //readyState===4 时说明请求已完成
            if(xhr.readyState === 4){
                if(xhr.status === 200 || xhr.status === 304){
                    //xhr.responseText用于获得字符串形式的响应数据
                    console.log(xhr.responseText);
                    callback(xhr.responseText);
                }
            }
        }
        xhr.send();//发起请求
    },
    //data应为'a=a1&b=b1'这种字符串格式，在jq里如果data为对象会自动将对象转成这种字符串格式
    post: function(url,data,callback){
        var xhr = new XMLHttpRequest();//XMLHttpRequest对象用于在后台与服务器交换数据
        xhr.open('POST',url,false);//设置发起参数
        xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');//添加http头，发送信息至服务器时内容编码类型。
        //当状态改变时触发函数
        xhr.onreadystatechange=function(){
            //readyState===4 时说明请求已完成
            if (xhr.readyState===4){
                if (xhr.status===200 || xhr.status===304){
                    //xhr.responseText用于获得字符串形式的响应数据
                    callback(xhr.responseText);
                }
            }
        }
        xhr.send(data);//发起请求
    }
}

//初始化国家列表
Ajax.get('/region/getCountryList',function (data) {
    console.log(data);
});

