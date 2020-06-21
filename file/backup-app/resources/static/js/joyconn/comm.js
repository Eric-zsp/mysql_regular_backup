/**
 * Created by Eric.Zhang on 2017/4/7.
 */
var joyconn_layout={};
joyconn_layout.Data={uid:""}
//ajax结果返回值验证，结果为空或失去登录或访问没有权限都会返回false，内部已做好弹出层提示
/**
 * 请求结果验证
 * @param data 服务器返回的数据
 * @param extNote 是否对除未登录、权限不足以外的验证做出提示，true表示自己进行提示，不传或false表示系统自动提示
 * @returns {boolean} true验证通过 false验证失败
 * @constructor
 */
joyconn_layout.ValidataResult=function (data,extNote) {
    if(!data||data==""){
        return false;
    }else if(data.code=="NoRule"){
        dialog({
            title: '访问失败',
            content: '您没有访问该数据的权限！',
            quickClose: true,
            cancel: false}).showModal();
        return false;

    }else if(data.code=='NoLogin'||data.code=='LoginFail'){
        dialog({
            title: '访问失败',
            content: '您已失去登录状态！请重新登录系统！',
            cancel: false,
            okValue:'去登陆',
            ok:function () {
                window.location.href='/page/account/login';
            }
        }).showModal();
        return false;
    }else if(data.code=="ServiceError"&&!extNote){
        dialog({
            title: '访问失败',
            content: '服务器暂时繁忙，请联系工作人员！',
            quickClose: true,
            cancel: false}).showModal();
        return false;

    }else if(data.code=="ParamsError"&&!extNote){
        var errmsg="请求的参数不正确！" + (data.errorMsg?data.errorMsg:"");
        dialog({
            title: '访问失败',
            content: errmsg,
            quickClose: true,
            cancel: false}).showModal();
        return false;

    }else{
        return true;
    }
}

joyconn_layout.getInArray=function(arr,attrName,value){
    var result = null;
    if(arr){
        $(arr).each(function (index,model) {
            if(model[attrName]==value){
                result=model;
                return false;
            }
        })
    }
    return result;
}

joyconn_layout.showLoadingDialog=function () {
    var dia_load=  dia_load = dialog({ content: '<i class="fa fa-spinner fa-spin"></i>' });;

    dia_load.showModal();
    return dia_load;
}
/**
 * Created by Eric.Zhang on 2017/5/3.
 */
function getPageCount( itemCount, pageSize) {
    return Math.ceil(itemCount / pageSize);

}
//显示分页。
//{elemetid:id，allcount:allcount，pno:1,pagesize:10}
function ShowPage(elementid, options, pageFuc) {
    if(options.allcount<= options.pagesize){
        return;
    }
    // $("#"+elementid).html('');
    $("#" + elementid).pagination(
        {
            coping: true,
            homePage: '首页',
            endPage: '末页',
            prevContent: '上一页',
            nextContent: '下一页',
            totalData: options.allcount,
            pageCount: getPageCount(options.allcount, options.pagesize),
            showData: options.pagesize,
            current: options.pno,

            // items_per_page:options.pagesize,//	每页显示的条目数	可选参数，默认是10
            // num_display_entries:5,//	连续分页主体部分显示的分页条目数	可选参数，默认是10
            // current_page:options.pno,//	当前选中的页面	可选参数，默认是0，表示第1页
            // prev_text :"« 上一页",	//“前一页”分页按钮上显示的文字	字符串参数，可选，默认是"Prev"
            // next_text: "下一页 »",//	“下一页”分页按钮上显示的文字	字符串参数，可选，默认是"Next"
            callback: function (api) {
                pageFuc(api.getCurrent());
                // if(index>0&&index<=getPageCount(options.pagesize,options.allcount)){
                //     pageFuc(index);
                // }
                //return false;
            }
        }
    );
    // maxentries	总条目数	必选参数，整数
    // items_per_page	每页显示的条目数	可选参数，默认是10
    // num_display_entries	连续分页主体部分显示的分页条目数	可选参数，默认是10
    // current_page	当前选中的页面	可选参数，默认是0，表示第1页
    // num_edge_entries	两侧显示的首尾分页的条目数	可选参数，默认是0
    // link_to	分页的链接	字符串，可选参数，默认是"#"
    // prev_text	“前一页”分页按钮上显示的文字	字符串参数，可选，默认是"Prev"
    // next_text	“下一页”分页按钮上显示的文字	字符串参数，可选，默认是"Next"
    // ellipse_text	省略的页数用什么文字表示	可选字符串参数，默认是"…"
    // prev_show_always	是否显示“前一页”分页按钮	布尔型，可选参数，默认为true，即显示“前一页”按钮
    // next_show_always	是否显示“下一页”分页按钮	布尔型，可选参数，默认为true，即显示“下一页”按钮
    // callback
    // var pageoptions={
    //     total:options.allcount,
    //     pageSize:options.pagesize,
    //     pageIndexName:options.pno,
    //     showFirstLastBtn:true,
    //     prevBtnText: "« 上一页",
    //     nextBtnText: "下一页 »",
    //     infoFormat: '{start} ~ {end} 共 {total} 条',
    //     showInfo: true
    //
    // };
    // //$("#"+elementid).html("");
    // //$("#"+elementid).page('destroy');
    //  $("#"+elementid).page(pageoptions).on("pageClicked", function (event, pageNumber) {
    //     //...
    //         pageFuc(pageNumber);
    // });
}


/**
 * 初始化 tip标签鼠标悬浮事件
 * @param iElement
 */
joyconn_layout.initTipsHover=function(parent){
    var tipElements = $(parent).find(".quick_help_note_icon");
    $(tipElements).each(function (i,tipElement) {
        var index = 0;
        $(tipElement) .mouseover(function () {
            var that = this;
            index = layer.tips($(that).text(), that, {
                tips: [2, '#428bca'],
                time: 0
            });
        });
        $(tipElement) .mouseout(function () {
            if(index){
                layer.close(index);
                index=0;
            }

        });
    });
}

joyconn_layout.showMenu=function () {
    var menuData = [];
    menuData.push({fName: '备份列表', fPic: 'fa fa-th-list', fType: 3, fLevel: 1,
        fUrl: 'javascript:void()', children: []});
    menuData.push({
        fName: '基础配置',
        fPic: 'fa fa-map-marker',
        fType: 3,
        fLevel: 1,
        fUrl: 'javascript:void()'
    });


    var tree=menuData;
    var curPageUrl=window.location.pathname;
    function fillMenu(container, model) {
        if (model.fType == 2||model.fType == 3) {
            if (model.fLevel == 1) {
                var _element = $(' <li class="treeview menu_node"><a href="#"> <i class="' + model.fPic + '"></i> <span>' + model.fName + '</span></a></li>');
                if (model.children&&model.children.length > 0) {
                    var ul = $('<ul class="treeview-menu"></ul>');
                    $(_element).append(ul);
                    $(model.children).each(function (index, _model) {
                        fillMenu(ul, _model);
                    });
                    _element.find('a').append(' <i class="fa fa-angle-left pull-right"></i>')
                }else{
                    _element.find('a').attr('href',model.fUrl)
                }
                $(container).append(_element);
            } else if (model.fLevel == 2) {
                var li = $('<li  class=""><a href="' + model.fUrl + ' "><i class="'+((!model.fPic||model.fPic=="#")?'fa fa-circle-o':model.fPic)+'"></i>' + model.fName + '</a></li>');
                $(container).append(li);
                if(model.fUrl.toLowerCase ()==curPageUrl.toLowerCase ()){
                    $(container).parent().addClass("active");
                    $(li).addClass("active");
                }
            }
        }
    }

    // var curInMenu=false;
    $(tree).each(function (index, model) {
        fillMenu($("#MAIN_NAVIGATION_MENU"), model);
    });


}

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



