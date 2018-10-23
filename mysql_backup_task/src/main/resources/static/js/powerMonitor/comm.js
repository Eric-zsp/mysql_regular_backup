/**
 * Created by Eric.Zhang on 2017/4/18.
 */
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

/**
 * csv file to 二维数组
 * 需要引入papaparse.js jschardet.js
 * @param callback
 * @returns {boolean}
 */
$.fn.csv2darr = function( callback ){
    if( typeof(FileReader) == 'undefined' ){    //if not H5
        alert("IE9及以下浏览器不支持，请使用Chrome或Firefox浏览器\nYour browser is too old,please use Chrome or Firefox");
        return false;
    }
    if( ! $(this)[0].files[0]){
        alert("请选择文件\nPlease select a file");
        return false;
    }
    var fReader = new FileReader();
    fReader.readAsDataURL( $(this)[0].files[0] );
    $fileDOM = $(this);
    fReader.onload = function(evt){
        var data = evt.target.result;
//        console.log( data );
        var encoding = checkEncoding( data );
//        console.log(encoding);
        //转换成二维数组，需要引入Papaparse.js
        Papa.parse( $($fileDOM)[0].files[0], {
            encoding: encoding,
            complete: function(results) {        // UTF8 \r\n与\n混用时有可能会出问题
//                console.log(results);
                var res = results.data;
                if( res[ res.length-1 ] == ""){    //去除最后的空行
                    res.pop();
                }
                callback && callback( res );
            }
        });
    }
    fReader.onerror = function(evt){
//        console.log(evt);
        alert("文件已修改，请重新选择(Firefox)\nThe file has changed,please select again.(Firefox)");
    }

    //检查编码，引用了 jschardet
    function checkEncoding( base64Str ){
        //这种方式得到的是一种二进制串
        var str = atob( base64Str.split(";base64,")[1] );
//        console.log(str);
        //要用二进制格式
        var encoding = jschardet.detect( str );
        encoding = encoding.encoding;
//        console.log( encoding );
        if( encoding == "windows-1252"){    //有时会识别错误（如UTF8的中文二字）
            encoding = "ANSI";
        }
        return encoding;
    }
}

function setDataRangerPick(containers, distinceType,startTime,endTime,callback) {

    $(containers).each(function (index, container) {
        var _dataTimeFormat = 'YYYY-MM-DD HH:mm:ss';
        var _separator = " 至 ";
        $(container).daterangepicker({
            applyClass: 'btn-sm btn-success',
            cancelClass: 'btn-sm btn-default',
            locale: {
                applyLabel: '确认',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                firstDay: 1
            },
            ranges: {
                //'最近1小时': [moment().subtract('hours',1), moment()],
                //                    '今日': [moment().startOf('day'), moment()],
                //                    '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                //                    '最近7日': [moment().subtract('days', 6), moment()],
                //                    '最近30日': [moment().subtract('days', 29), moment()],
                //                    '本月': [moment().startOf("month"),moment().endOf("month")],
                //                    '上个月': [moment().subtract(1,"month").startOf("month"),moment().subtract(1,"month").endOf("month")]
            },
            opens: 'left',    // 日期选择框的弹出位置
            separator: _separator,
            showWeekNumbers: true,     // 是否显示第几周
            endDate: moment(),
            startDate: moment().subtract(distinceType, 1),

            timePicker: true,
            timePickerIncrement: 5, // 时间的增量，单位为分钟
            timePicker12Hour: false, // 是否使用12小时制来显示时间


            //maxDate : moment(),           // 最大时间
            format: _dataTimeFormat

        }, function (start, end, label) { // 格式化日期显示框

            $(container).siblings(".p_btime").val(start.format(_dataTimeFormat));
            $(container).siblings(".p_etime").val(end.format(_dataTimeFormat));
            if(callback){
                callback();
            }
        }).prev().on('click', function () {
            $(this).next().focus();
        });
        var _start =(startTime?startTime: moment().subtract(distinceType, 1));
        var _end = (endTime?endTime:moment());
        $(container).siblings("input[data-name='p_btime']").val(_start.format(_dataTimeFormat));
        $(container).siblings("input[data-name='p_etime']").val(_end.format(_dataTimeFormat));
        $(container).val(_start.format(_dataTimeFormat) + _separator + _end.format(_dataTimeFormat));
    });

}

function setDataTimePickTouch(containers) {
    $(containers).each(function (index, container) {
        if ($(container).attr("data-widget") == "data-picker") {
            $(container).calendar({
                onChange: function (picker, values, displayValues) {
                    console.log(values);
                }
            });

        } else if ($(container).attr("data-widget") == "datetime-picker") {
            $(container).datetimePicker({
                onChange: function (picker, values, displayValues) {
                    console.log(values);
                }
            });
            //$(container).datetimePicker();
        }
    });
}



//monthParam {startYear:2016, startMonth:1, endYear:2017, endMonth:2}
function setMonthRangePick(input, monthParam,monthChange,monthComplate) {
    var startYear=2016;var  startMonth =1;var endYear=2017;var endMonth =2;
    if(monthParam){
        if(monthParam.startYear){
            startYear=monthParam.startYear;
        }
        if(monthParam.startMonth){
            startMonth=monthParam.startMonth;
        }
        if(monthParam.endYear){
            endYear=monthParam.endYear;
        }
        if(monthParam.endMonth){
            endMonth=monthParam.endMonth;
        }
    }

    function absPos(node){
        var x=y=0;
        do{
            x+=node.offsetLeft;
            y+=node.offsetTop;
        }while(node=node.offsetParent);
        return{
            'x':x,
            'y':y
        };
    }

    var content = $('<div class="row" ></div>');
    var container=$('<div class="daterangepicker dropdown-menu show-calendar opensright" style="display: none;width:500px;position: absolute;"><div class="content"><div class="col-md-12"></div></div></div>');
    $(container).find(".col-md-12").append(content);
    var monthName = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];
    if (endYear >= startYear) {
        endYear = startYear;
        if (startMonth > endMonth) {
            startMonth = endMonth;
        }
    }
    for (var a = 0; a < 2; a++) {
        //左右两个月份面板
        var monthPanel = $('<div class="col-md-6" data="' + (a == 0 ? "left" : "right") + '" ></div>');
        $(content).append(monthPanel);
        $(monthPanel).append('  <div class="row" style="margin-bottom: 10px;">\n' +
            '                    <div class="col-md-3 "><i class="fa  fa-chevron-left"></i></div>\n' +
            '                    <div class="col-md-6 text-center"><span class="' + (a == 0 ? "left_year" : "right_year") + '">' + startYear + '</span></div>\n' +
            '                    <div class="col-md-3 text-right"><i class="fa  fa-chevron-right"></i></div>\n' +
            '                </div>\n' +
            '               <div class="row">\n' +
            '                    <div class="col-md-12 month_btn_panel_container"></div>\n' +
            '                </div>\n');
        //填充月份
        var row_div;
        for (var b = 1; b < 13; b++) {
            if (b % 4 == 1) {
                row_div = $('<div class="row" style="margin-top: 10px;padding-right: 10px;"></div>');
                $(monthPanel).find(".month_btn_panel_container").append(row_div);
            }
            var month_btn = $(' <div class="col-md-3  text-center"><div class="btn month" data="' + b + '">' + monthName[b - 1] + '</div></div>');
            if ((a == 0 && startMonth == b) || (a == 1 && endMonth == b)) {
                $(month_btn).find(".btn").addClass("bg-light-blue");
            }
            $(row_div).append(month_btn);
        }

    }
    //添加确定、取消按钮
    $(content).append('<div class="col-md-12" style="margin-top: 20px;"> <div class="row"><div class="col-md-6 text-right"><span class="btn bg-gray margin sel_btn_cancel">取消</span></div>\n' +
        '              <div class="col-md-6"><span class="btn bg-olive margin sel_btn_ok">确定</span></div></div></div>');

    //月份书标悬浮事件
    $(content).find(".col-md-6 .month").each(function (i, month_btn) {
        $(month_btn).mouseover(function () {
            if ($(month_btn).hasClass('.bg-light-blue')) {
                return;
            }
            $(month_btn).addClass("bg-gray")
        }).mouseout(function () {
            if ($(month_btn).hasClass('.bg-light-blue')) {
                return;
            }
            $(month_btn).removeClass("bg-gray")
        })


    });
    $(content).find(".col-md-6[data='left'] .month").each(function (i, month_btn) {
        $(month_btn).click(function () {
            $(month_btn).parents(".month_btn_panel_container").find(".month").removeClass("bg-light-blue");
            $(month_btn).addClass("bg-light-blue");
            startMonth=parseInt($(month_btn).attr("data"));
        });
    })

    $(content).find(".col-md-6[data='right'] .month").each(function (i, month_btn) {
        $(month_btn).click(function () {
            $(month_btn).parents(".month_btn_panel_container").find(".month").removeClass("bg-light-blue");
            $(month_btn).addClass("bg-light-blue");
            endMonth=parseInt($(month_btn).attr("data"));
        });
    });
    //年份切换事件
    $(content).find('.col-md-6[data="right"] .fa-chevron-left').click(function () {
        endYear-=1
        $(this).html(endYear);
    });
    $(content).find('.col-md-6[data="right"] .fa-chevron-right').click(function () {
        endYear+=1
        $(this).html(endYear);
    });
    $(content).find('.col-md-6[data="left"] .fa-chevron-left').click(function () {
        startYear-=1
        $(this).html(startYear);
    });
    $(content).find('.col-md-6[data="left"] .fa-chevron-right').click(function () {
        startYear+=1
        $(this).html(startYear);
    });
    $(content).find('.fa-chevron-right').click(function () {
        startYear=parseInt($(this).text());

    });

    $(content).find(".sel_btn_cancel").click(function(){
        $(container).hide();
    });
    $(content).find(".sel_btn_ok").click(function(){
        if(monthChange){
            monthChange({startYear:startYear, startMonth:startMonth, endYear:endYear, endMonth:endMonth});
        }
        $(input).val(startYear+'-'+(startMonth<10?"0":"")+startMonth+" 至 "+endYear+"-"+(endMonth<10?"0":"")+endMonth);
        $(container).hide();
    });
    $("body").append(container);
    $(input).click(function(){
        var pos=absPos($(input).get(0));
        $(container).css("left",pos.x+'px').css("top",(pos.y+$(input).outerHeight()+2)+'px');
        $(container).show();
    });
    $(input).val(startYear+'-'+(startMonth<10?"0":"")+startMonth+" 至 "+endYear+"-"+(endMonth<10?"0":"")+endMonth);
    if(monthComplate){
        monthComplate({startYear:startYear, startMonth:startMonth, endYear:endYear, endMonth:endMonth});
    }
}
function setMonthRangePick_mobile(input, monthParam,monthChange){

    $(input).picker({
        title: "时间段",
        cols: [
            {
                textAlign: 'center',
                values: ['2010','2011','2012','2013','2014','2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', '2024', '2025', '2026', '2027', '2028', '2029', '2030']
                //如果你希望显示文案和实际值不同，可以在这里加一个displayValues: [.....]
            },
            {
                textAlign: 'center',
                values: ['-']
            },
            {
                textAlign: 'center',
                values: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']
            }, {
                textAlign: 'center',
                values: ['至']
            },
            {
                textAlign: 'center',
                values: ['2010','2011','2012','2013','2014','2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', '2024', '2025', '2026', '2027', '2028', '2029', '2030']
            }            ,  {
                textAlign: 'center',
                values: ['-']
            },
            {
                textAlign: 'center',
                values: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']
            }
        ],
        onChange: function(p, v, dv) {
            //  console.log(p, v, dv);
        },
        onClose: function(p, v, d) {
            if(monthChange){
                //参数：startYear,startMonth,endYear,endMonth
                monthChange(p.value[0],p.value[2],p.value[4],p.value[6]);
            }
            //console.log("close");
        }
    });
    if(monthParam){
        var startYear=2016;var  startMonth =1;var endYear=2017;var endMonth =2;
        if(monthParam.startYear){
            startYear=monthParam.startYear;
        }
        if(monthParam.startMonth){
            startMonth=monthParam.startMonth;
        }
        if(monthParam.endYear){
            endYear=monthParam.endYear;
        }
        if(monthParam.endMonth){
            endMonth=monthParam.endMonth;
        }
        $(input).picker("setValue", [startYear, startMonth, endYear,endMonth]);
    }
}

function NagativeIframe(container, url, param, marginHeight) {

    param = param + '&md5key=' + $.md5(param + '&key1231231=1');
    var iframe = $('<iframe src="' + url + '?' + param + '" width="100%" height="100%" style="border: 0px;overflow: hidden" scrolling="no"></iframe>');
    $(container).html(iframe);
    $(iframe).height($(".content-wrapper").height() - marginHeight);
    window.onresize = function () {
        $(iframe).height($(".content-wrapper").height() - marginHeight);
    }
}


function forwardToConfigRoomList(companyCD, projectCD, roomCD,url) {
    if (companyCD) {
        $.cookie('power_company', companyCD, {path: '/'});
    }
    if (projectCD) {
        $.cookie('power_project', projectCD, {path: '/'});
    }
    if (roomCD) {
        $.cookie('power_room', roomCD, {path: '/'});
    }
    if(url){
        if(window.location.href==url){
            location.reload();
        }else{
            window.location = url;
        }

    }else{

        window.location = '/page/room/defualt';
    }
}

function forwardToAlarmRoomList(companyCD, projectCD, roomCD) {
    if (companyCD) {
        $.cookie('power_company', companyCD, {path: '/'});
    }
    if (projectCD) {
        $.cookie('power_project', projectCD, {path: '/'});
    }
    if (roomCD) {
        $.cookie('power_room', roomCD, {path: '/'});
    }
    window.location = '/page/WorkPage/ManageAlarm/index';
}

function timeStampToFormatString(second_time) {

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

function initUserGroupCDSelect(containers,CompanyCD,select_groupcd){
    $.get("/api/ManageApi/UserManageApi/selectGroupCDByParam",{CompanyCD:CompanyCD},function (data) {
        if (mortise_layout.ValidataResult(data)) {
            if (data.result) {
                $(data.result).each(function (index,groupcd) {
                    $(containers).each(function(a,container){
                        $(container).append('<option value="'+groupcd+'" '+ (select_groupcd&&select_groupcd==groupcd?"selected":"")+'>'+groupcd+'</option>');
                    })

                });
            }
        }
    });
}
//能耗换算成千克煤
function getCoalFromEnergy(energy) {
    return parseInt(energy * 3600 / 20908);
}

//能耗换算成碳排放
function getCO2FromEnergy(energy) {
    return parseInt(getCoalFromEnergy(energy) / 1.471);
}

//根据用户ID数组获取用户信息
function getUsers(uids,callback){
    if(uids&&uids.length>0){
        $.get("/api/UserApi/getUsers",{uids:uids},function(data){
            if (mortise_layout.ValidataResult(data)) {
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
function getCompanyUsers(CompanyCD,callback){
    $.get("/api/UserApi/selectUsersByParam",{CompanyCD:CompanyCD,pageIndex:1,pageSize:1000000},function(data){
        if (mortise_layout.ValidataResult(data)) {
            if (data.result) {
                if(callback){
                    callback(data.result);
                }
            }
        }
    })
}

var globle_compay_project_room_data={companys:null,peojects:{},roomcs:{}};
function getMyCompanys(callback) {
    $.get("/api/ConfigApi/CompanyApi/getMyCompanyList",{},function(data){
        if (mortise_layout.ValidataResult(data)) {
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
function getMyProjects(companycd,callback) {
    $.get("/api/ConfigApi/ProjectApi/getMyLevelProjectList",{companyCD:companycd},function(data){
        if (mortise_layout.ValidataResult(data)) {
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
function getMyRoomsByProjectCD(companycd,projectcd,callback) {
    $.get("/api/ConfigApi/RoomApi/getMyLvlRoomListByProjectCD",{companyCD:companycd,projectCD:projectcd},function(data){
        if (mortise_layout.ValidataResult(data)) {
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


//region 尖峰平谷
var jianfengpingguObj_operat={}
jianfengpingguObj_operat.valueNames= [
    {value: 1, name: '低谷'},
    {value: 2, name: '谷'},
    {value: 3, name: '平'},
    { value: 4, name: '峰' },
    {value: 5, name: '尖'}]
jianfengpingguObj_operat.getName = function (value) {
    var result = '';
    $(jianfengpingguObj_operat.valueNames).each(function (i, vn) {
        if (vn.value == value) {
            result = vn.name;
            return false;
        }
    });
    return result;
}
//endregion


function initRoomChangePannel(container) {
    if($(container).attr("loaded")!="loaded") {
        getMyCompanys(function (companys) {
            if (companys) {
                $(container).empty();
                $(container).append('<li class="header">切换场所</li>');
                $(companys).each(function (i, companyModel) {
                    var li = $('<li class="treeview" >\n' +
                        '              <a href="javascript:void(0)">\n' +
                        '                <i class="fa fa-laptop"></i><span title="'+companyModel.p_companydescription +'">' + companyModel.p_companydescription + '</span><i class="fa fa-angle-left pull-right"></i>\n' +
                        '              </a>\n' +
                        '              <ul class="treeview-menu project_list">\n' +
                        '                       <li class="treeview"><a href="#"><i class="fa fa-spinner fa-spin"></i></a></li>'+
                        '              </ul>\n' +
                        '            </li>');
                    $(container).append(li);
                    $(li).click(function () {
                        if($(li).hasClass('active')){
                            $(li).removeClass('active')
                        }else{
                            $(li).addClass('active')
                            loadProjects(companyModel.p_companycd, $(li).find('.project_list'));
                        }
                    })

                })
            }
        });
    }
        function loadProjects(companycd,projectContainer) {
            if ($(projectContainer).attr("loaded") != "loaded") {
                getMyProjects(companycd, function (projects) {
                    if (projects) {
                        $(projectContainer).empty();
                        $(projects).each(function (i, projectModel) {
                            var li = $('<li >\n' +
                                '                  <a href="javascript:void(0)"  title="'+projectModel.p_projectdescription +'"><i class="fa fa-cubes"></i> ' + projectModel.p_projectdescription + ' <i class="fa fa-angle-left pull-right"></i></a>\n' +
                                '                  <ul class="treeview-menu room_list">\n' +
                                '                       <li class="treeview"><a href="#"><i class="fa fa-spinner fa-spin"></i></a></li>'+
                                '                  </ul>\n' +
                                '                </li>');
                            $(projectContainer).append(li);
                            $(li).click(function () {
                                if($(li).hasClass('active')){
                                    $(li).removeClass('active')
                                }else{
                                    $(li).addClass('active');
                                    loadRooms(companycd, projectModel.p_projectcd, $(li).find('.room_list'));
                                }
                                return false;
                            })

                        })
                    }
                });
            }
        }
        function loadRooms(companycd,projectcd,roomContainer) {
            if ($(roomContainer).attr("loaded") != "loaded") {
                getMyRoomsByProjectCD(companycd,projectcd, function (rooms) {
                    if (rooms) {
                        $(roomContainer).empty();
                        $(rooms).each(function (i, roomModel) {
                            var li = $('<li><a href="javascript:void(0)"  title="'+roomModel.p_roomdescription +'"><i class="fa fa-h-square"></i> '+roomModel.p_roomdescription+'</a></li>');
                            $(roomContainer).append(li);
                            $(li).click(function () {
                                forwardToConfigRoomList(companycd,projectcd,roomModel.p_roomcd,window.location.href);
                                return false;
                            })

                        })
                    }
                });
            }
        }

}
$(function () {

    $(".homework-sysconfig-logo-name").html("<b>能源</b>管理");
    $(".homework-sysconfig-logo-mini-name").html("能源");
    $(".homework-sysconfig-name").html("能源管理系统");
    $("title").html($("title").html() + "能源管理系统");
})