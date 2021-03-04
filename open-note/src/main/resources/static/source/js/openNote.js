//入口函数
$(function(){
    vm.init();//初始化网页，包括动态显示用户名和loginButton的文本，动态修改loginButton的href属性。
});

let vm = new Vue({
    el: '#headerContainer',//el是element的缩写，意为选择器
    data: {
        username: '',//用于显示用户名
        loginButton: '登录',//用于显示login按钮的文本
        loginButtonHref: 'page/login/index.html'//用于动态更改loginButton的href属性
    },
    methods: {
        //初始化网页
        init: function () {
            //发起post方式的ajax请求，用于动态显示用户名和loginButton的文本，动态修改loginButton的href属性。
            $.post("user/getCurrentLoginUsername", function (data) {
                if (data !== ''){
                    vm.username = data;
                    vm.loginButton = '登出';
                    vm.loginButtonHref = '/user/logout';//发往Spring security的logout接口。（Spring security会重定向到首页）
                }else {
                    vm.loginButton = '登录';
                    vm.loginButtonHref = 'page/login/index.html';
                }
            });
        }
    }
})
