//表单校验（不能放在后面，原因暂时未知，这是样板，暂时未自定义校验功能）
(function () {
    'use strict'

    window.addEventListener('load', function () {
        // 提取所有我们要应用自定义Bootstrap验证样式的表单
        const forms = document.getElementsByClassName('needs-validation');

        //遍历它们并阻止提交
        Array.prototype.filter.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (form.checkValidity() === false) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                form.classList.add('was-validated')
            }, false)
        })
    }, false)
}())

//入口函数
$(function(){
    vm.init();//初始化网页
});

//countryDto对象的构造函数
function CountryDto(nameCn) {
    this.nameCn = nameCn;
}
//城市对象的构造函数
function CityDto(nameCn,nameEn,stateCn,stateEn,countryDto) {
    this.nameCn = nameCn;
    this.nameEn = nameEn;
    this.stateCn = stateCn;
    this.stateEn = stateEn;
    this.countryDto = countryDto;
}
//用户对象的构造函数
function UserDto(username,email,gender,phoneNumber,surname,firstName,address,cityDto){
    this.username = username;
    this.email = email;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
    this.surname = surname;
    this.firstName = firstName;
    this.address = address;
    this.cityDto = cityDto;
}

let countryDto = new CountryDto('')
let cityDto = new CityDto('','','','',countryDto)
let userDto = new UserDto('','','',null,'','','',cityDto)

let vm = new Vue({
    el: '#personalData',
    data: {
        promptBoxModalBody: '',//弹出框的内容主体
        currentLoginUsername: '',//当前登录用户的用户名
        user: userDto,//将userDto对象和vm的user property绑定起来
        regionData: {//地区数据
            'cityMap': {
                'countryIdList': [],
                'stateEnList': [],
                'stateCnList': [],
                'nameCnList': [],
                'nameEnList': []
            },
            'countryArray': []//按countryId升序排列
        },
        countryList: [],//国家列表
        stateList: [],//省份列表
        cityList: []//城市列表
    },
    methods: {
        //初始化网页
        init: function () {
            //得到用户及地区信息
            $.get('/user/getUserAndRegionByCurrentLogin', function (data) {
                //将返回的数据赋值给vm.user。（Vue会将user和userDto绑定起来）
                if (data.cityDto !== null){
                    vm.user.cityDto.countryDto.nameCn = data.cityDto.countryDto.nameCn;
                    vm.user.cityDto.nameCn = data.cityDto.nameCn;
                    vm.user.cityDto.nameEn = data.cityDto.nameEn;
                    vm.user.cityDto.stateCn = data.cityDto.stateCn;
                    vm.user.cityDto.stateEn = data.cityDto.stateEn;
                }
                vm.user.username = data.username;
                vm.user.email = data.email;
                vm.user.gender = data.gender;
                vm.user.phoneNumber = data.phoneNumber;
                vm.user.surname = data.surname;
                vm.user.firstName = data.firstName;
                vm.user.address = data.address;

                //设置当前用户名
                vm.currentLoginUsername = vm.user.username;

                //若返回的性别为null，则赋其值为“保密”。
                if(vm.user.gender === null){
                    vm.user.gender = '保密';
                }

                //加载regionData.json文件，将数据赋值到vm.regionData上。然后初始化地区信息。
                $.getJSON('../../source/data/region/regionData.json', function(data) {
                    vm.regionData = data;
                    vm.countryList = vm.regionData.countryArray;//初始化地区列表
                    vm.updateStateListSetByCountryName(vm.user.cityDto.countryDto.nameCn);
                    if (vm.user.cityDto.stateCn != null){
                        vm.updateCityListByStateName(vm.user.cityDto.stateCn);
                    }else if (vm.user.cityDto.stateEn != null){
                        vm.updateCityListByStateName(vm.user.cityDto.stateEn);
                    }
                });
            });
        },
        //通过countryName得到有哪些省份。（若查询不到省份数据，则直接更新城市数据）
        updateStateListSetByCountryName: function (countryName) {
            vm.stateList = [];//更新前先对原数据进行清空操作

            let countryId = vm.regionData.countryArray.indexOf(countryName) + 1;//得到指定地区名所对应的countryId
            let countryIdList = vm.regionData.cityMap.countryIdList;
            let stateCnList = vm.regionData.cityMap.stateCnList;
            let stateEnList = vm.regionData.cityMap.stateEnList;
            let matched = false;//表示countryId与countryIdList[i]匹配过了
            let currentStateName = null;//当前的stateName。用于排除重复名。

            //循环遍历countryIdList
            for (let i = 0, length = countryIdList.length; i < length; i++){
                //countryId与countryIdList[i]匹配，将对应的省份数据装入vm.stateList。若已经添加过同个省名，则不继续装载。
                if (countryId === countryIdList[i]){
                    //如果有中文版则使用中文，否则使用英文版。
                    if (stateCnList[i] != null){
                        if (stateCnList[i] !== currentStateName){
                            currentStateName = stateCnList[i];//更新当前stateName
                            vm.stateList.push(currentStateName);
                        }
                    }else if (stateEnList[i] != null){
                        if (stateEnList[i] !== currentStateName){
                            currentStateName = stateEnList[i];
                            vm.stateList.push(currentStateName);
                        }
                    }
                    //表示countryId与countryIdList[i]匹配过了
                    matched = true;
                }
                //若countryId与countryIdList[i]匹配过了，且当前不匹配，则终止循环。
                if (matched === true && countryId !== countryIdList[i]){
                    break;
                }
            }

            vm.cityList = [];//无论如何都要对citySet做清空操作，因为countryName已经发生变化。

            //若执行上面的代码后，vm.stateList仍为空，则直接更新城市数据。
            if (vm.stateList[0] === undefined){
                vm.updateCityListByCountryName(countryName);
            }
        },
        //通过countryName得到有哪些市（部分country没有省份，所以需要直接更新cityList）
        updateCityListByCountryName: function(countryName){
            vm.stateList = [];//调用本方法意为着没有省份数据
            vm.cityList = [];//更新前先对原数据进行清空操作

            let countryIdList = vm.regionData.cityMap.countryIdList;
            let nameCnList = vm.regionData.cityMap.nameCnList;
            let nameEnList = vm.regionData.cityMap.nameEnList;
            let countryId = vm.regionData.countryArray.indexOf(countryName) + 1;//得到指定地区名所对应的countryId

            //循环遍历countryIdList
            for (let i = 0, length = countryIdList.length; i < length; i++){
                //countryId与countryIdList[i]匹配，将对应的cityName装入vm.cityList。
                if (countryId === countryIdList[i]){
                    if (nameCnList != null){
                        vm.cityList.push(nameCnList[i]);
                    }else {
                        vm.cityList.push(nameEnList[i]);
                    }
                }
            }
        },
        //通过省名得到有哪些市
        updateCityListByStateName: function (stateName) {
            vm.cityList = [];//更新前先对原数据进行清空操作

            let stateCnList = vm.regionData.cityMap.stateCnList;
            let stateEnList = vm.regionData.cityMap.stateEnList;
            let nameCnList = vm.regionData.cityMap.nameCnList;
            let nameEnList = vm.regionData.cityMap.nameEnList;

            let matched = false;//表示匹配过中文或英文的省名

            for (let i = 0, length = stateCnList.length; i < length; i++){
                //若匹配到中文或英文的省名，则装载城市名。
                if (stateName === stateCnList[i] || stateName === stateEnList[i]){
                    //如果有中文版则使用中文，否则使用英文版。
                    if (nameCnList[i] != null){
                        vm.cityList.push(nameCnList[i]);
                    }else {
                        vm.cityList.push(nameEnList[i]);
                    }
                    matched = true;
                }
                //若匹配过中文或英文的省名，且当且无法匹配到，则终止循环。
                if (matched === true && stateName !== stateCnList[i] && stateName !== stateEnList[i]){
                    break;
                }
            }
        },
        //提交当前用户信息给updateUser接口，返回true表示更新成功，false表示失败。
        updateUser: function () {
            let activationPromptBox = document.getElementById("activationPromptBox");//取得激活提示框元素
            //填写用于提交的表单
            let param = {
                'username':vm.user.username,
                'gender':vm.user.gender,
                'phoneNumber':vm.user.phoneNumber,
                'surname':vm.user.surname,
                'firstName':vm.user.firstName,
                'address':vm.user.address,
                'cityDto':{
                    'nameCn':vm.user.cityDto.nameCn,
                    'nameEn':vm.user.cityDto.nameEn,
                    'stateCn':vm.user.cityDto.stateCn,
                    'stateEn':vm.user.cityDto.stateEn,
                    'countryDto':{
                        'nameCn':vm.user.cityDto.countryDto.nameCn
                    }
                }
            };
            //发起ajax请求
            $.ajax({
                type: 'post',
                url: '/user/updateUser',
                dataType: 'json',
                contentType:"application/json",
                data: JSON.stringify(param),
                success: function (data) {
                    if (data){
                        //使用模态框提示用户信息更新成功
                        vm.promptBoxModalBody = '修改成功';
                        activationPromptBox.click();
                    }else {
                        //使用模态框提示用户信息更新失败
                        vm.promptBoxModalBody = '修改失败';
                        activationPromptBox.click();
                    }
                }
            })
        },
        //使用ajax检测username是否存在，返回值若为true即该用户名存在。
        checkIfTheUserExists: function (username) {
            let repeat = false;
            $.ajax({
                url: '/user/checkIfTheUserExists',
                type: 'post',
                data: {'username':username},
                async: false,//不采用异步方式，即用同步方式。（默认为true）
                success: function (data) {
                    repeat = data;//得到是否重复
                }
            })
            return repeat;
        }
    },
    computed: {
        //动态更新地区联动数据（这里用到了Vue的getter和setter功能）
        countryNameByComputed: {
            get: function(){
                return this.user.cityDto.countryDto.nameCn;
            },
            set: function(newCountryName){
                //调整user的地区数据
                let cityDto = this.user.cityDto;
                cityDto.countryDto.nameCn = newCountryName;
                cityDto.stateCn = null;
                cityDto.stateEn = null;
                cityDto.nameCn = null;
                cityDto.nameEn = null;
            }
        },
        stateNameByComputed: {
            get: function () {
                if (this.user.cityDto.stateCn != null){
                    return this.user.cityDto.stateCn;
                }else {
                    return this.user.cityDto.stateEn;
                }
            },
            set: function (newStateName) {
                //调整user的地区数据
                let cityDto = this.user.cityDto;
                cityDto.stateCn = newStateName;
                cityDto.stateEn = newStateName;
                cityDto.nameCn = null;
                cityDto.nameEn = null;
            }
        },
        cityNameByComputed: {
            get: function(){
                if (this.user.cityDto.nameCn != null){
                    return this.user.cityDto.nameCn;
                }else {
                    return this.user.cityDto.nameEn;
                }
            },
            set: function (newCityName) {
                this.user.cityDto.nameCn = newCityName;
                this.user.cityDto.nameEn = newCityName;
            }
        }
    },
    watch: {
        countryNameByComputed: function () {
            this.updateStateListSetByCountryName(this.user.cityDto.countryDto.nameCn);
        },
        stateNameByComputed: function () {
            if (this.user.cityDto.stateCn != null){
                this.updateCityListByStateName(this.user.cityDto.stateCn);
            }else if(this.user.cityDto.stateEn != null){
                this.updateCityListByStateName(this.user.cityDto.stateEn);
            }
        },
        //当用户名发生改变时，检测用户名是否重复，若重复，则显示usernameTips。
        'user.username': {
            handler: function () {
                let usernameTips = document.getElementById('usernameTips');
                let repeat = this.checkIfTheUserExists(this.user.username);

                //当用户名重复，且改变的用户名于当前用户名不同时，显示usernameTips
                if (repeat && (this.user.username !== this.currentLoginUsername)){
                    usernameTips.setAttribute('style','display: inline');
                }else {
                    usernameTips.setAttribute('style','display: none');
                }
            }
        }
    }
})