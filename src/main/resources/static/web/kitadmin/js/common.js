

function openWindow(id, title, url, width, height){
    var window_width = width ? width : '80%';
    var window_height = height ? height : '80%';
    layer.open({
        id: id,
        type: 2,
        area: [window_width, window_height],
        fix: false,
        maxmin: true,
        shadeClose: false,
        shade: 0.4,
        title: title,
        content: url
    });
}

/*关闭弹出框口*/
function closeWindow(windowName){
    var index = parent.layer.getFrameIndex(windowName);
    parent.layer.close(index);
}


$.ajaxSetup({

    error:function(e){
        layer.alert("发生错误！", {icon: 6});
    }
});



