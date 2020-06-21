var joyconn_core={}
Date.prototype.format = function (format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}
Date.prototype.addday = function (value) {
    value *= 1;
    if (isNaN(value)) {
        value = 0;
    }
    var newDate
        = new Date(Date.parse(this) + value * 24 * 60 * 60 * 1000);


    return newDate;
}
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.endWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substring(this.length - s.length) == s)
        return true;
    else
        return false;
    return true;
}
String.prototype.startWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substr(0, s.length) == s)
        return true;
    else
        return false;
    return true;
}


//根据用户ID数组获取用户信息
joyconn_core.getUsers= function (uids,callback){
    if(uids&&uids.length>0){
        $.post("/api/ManageApi/UserManageApi/getUsers",{uids:uids},function(data){
            if (joyconn_layout.ValidataResult(data)) {
                if (data.result) {
                    if(callback){
                        callback(data.result);
                    }
                }
            }
        })
    }

}


//根据用户ID数组获取用户信息
joyconn_core. getCompanyUsers=function(CompanyCD,callback){
    $.get("/api/UserApi/selectUsersByParam",{CompanyCD:CompanyCD,pageIndex:1,pageSize:1000000},function(data){
        if (joyconn_layout.ValidataResult(data)) {
            if (data.result) {
                if(callback){
                    callback(data.result);
                }
            }
        }
    })
}

joyconn_core. getMyCompanys=function(callback) {
    $.get("/api/ConfigApi/CompanyApi/getMyCompanyList",{},function(data){
        if (joyconn_layout.ValidataResult(data)) {
            if (data.result) {
                if(callback){
                    callback(data.result);
                }
            }else{
                if(callback){
                    callback([]);
                }
            }
        }
    })
}
joyconn_core. getMyProjects=function(companycd,callback) {
    $.get("/api/ConfigApi/ProjectApi/getMyLevelProjectList",{companyCD:companycd},function(data){
        if (joyconn_layout.ValidataResult(data)) {
            if (data.result) {
                if(callback){
                    callback(data.result);
                }
            }else{
                if(callback){
                    callback([]);
                }
            }
        }
    })

}
joyconn_core. getMyRoomsByProjectCD=function(companycd,projectcd,callback) {
    $.get("/api/ConfigApi/RoomApi/getMyLvlRoomListByProjectCD",{companyCD:companycd,projectCD:projectcd},function(data){
        if (joyconn_layout.ValidataResult(data)) {
            if (data.result) {
                if(callback){
                    callback(data.result);
                }
            }else{
                if(callback){
                    callback([]);
                }
            }
        }
    })

}

joyconn_core. timeStampToFormatString=function(second_time) {

    var time = parseInt(second_time) + "秒";
    if (parseInt(second_time) > 60) {

        var second = parseInt(second_time) % 60;
        var min = parseInt(second_time / 60);
        time = min + "分" + second + "秒";

        if (min > 60) {
            min = parseInt(second_time / 60) % 60;
            var hour = parseInt(parseInt(second_time / 60) / 60);
            time = hour + "小时" + min + "分" + second + "秒";

            if (hour > 24) {
                hour = parseInt(parseInt(second_time / 60) / 60) % 24;
                var day = parseInt(parseInt(parseInt(second_time / 60) / 60) / 24);
                time = day + "天" + hour + "小时" + min + "分" + second + "秒";
            }
        }


    }

    return time;
}

joyconn_core.parseTime=function(strdate){
    var arr = strdate.split(/[- : \/]/);
    if(arr.length>5){
        return new Date(arr[0], arr[1]-1, arr[2], arr[3], arr[4], arr[5]);
    }else if(arr.length==5){
        return new Date(arr[0], arr[1]-1, arr[2], arr[3], arr[4]);
    }else if(arr.length==4){
        return new Date(arr[0], arr[1]-1, arr[2], arr[3]);
    }else if(arr.length==3){
        return new Date(arr[0], arr[1]-1, arr[2]);
    }
}

//region 账单支付状态
joyconn_core.bill={}
joyconn_core.bill.ChargeStatus_operat={valueNames:[
        {value: 0, name: '待支付'},
        {value: 1, name: '已支付'},
        {value: 2, name: '订单处理中'},
        {value: 3, name: '已完成'}],getName:function (value) {
        var result = '';
        $(joyconn_core.bill.ChargeStatus_operat.valueNames).each(function (i, vn) {
            if (vn.value == value) {
                result = vn.name;
                return false;
            }
        });
        return result;
    }}

joyconn_core.bill.ChargePayType_operat={valueNames:[
        {value: 1, name: '微信支付'},
        {value: 2, name: '支付宝支付'},
        {value: 3, name: '后台充值'},
        {value: 4, name: '系统赠送'},
        {value: 4, name: '消费失败退款'}],getName:function (value) {
        var result = '';
        $(joyconn_core.bill.ChargePayType_operat.valueNames).each(function (i, vn) {
            if (vn.value == value) {
                result = vn.name;
                return false;
            }
        });
        return result;
    }}



//endregion


$(function () {

    // $(".homework-sysconfig-logo-name").html("<b>洗车</b>平台");
    // $(".homework-sysconfig-logo-mini-name").html("洗车");
    $(".homework-sysconfig-name").html("洗车平台");
    $("title").html($("title").html() + "洗车平台");
})