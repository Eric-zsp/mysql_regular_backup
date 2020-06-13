/**
 * Created by Eric.Zhang on 2017/4/7.
 */
var mortise_layout={};
mortise_layout.Data={uid:""}
//ajax结果返回值验证，结果为空或失去登录或访问没有权限都会返回false，内部已做好弹出层提示
/**
 * 请求结果验证
 * @param data 服务器返回的数据
 * @param extNote 是否对除未登录、权限不足以外的验证做出提示，true表示自己进行提示，不传或false表示系统自动提示
 * @returns {boolean} true验证通过 false验证失败
 * @constructor
 */
mortise_layout.ValidataResult=function (data,extNote) {
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

mortise_layout.getInArray=function(arr,attrName,value){
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

mortise_layout.showLoadingDialog=function () {
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
mortise_layout.initTipsHover=function(parent){
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

mortise_layout.showMenu=function () {
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

$(function () {

});