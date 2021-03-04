let testEditor;
let noteId = 0;//为0时表示进入添加默认，为其它数值时则是查看指定noteId的笔记。

//noteDto的构造函数
function NoteDto(title,fileType,describe,content){
    this.title = title;
    this.fileType = fileType;
    this.describe = describe;
    this.content = content;
}

//编辑器构建方法
function structureEditor(markdown){
    testEditor = editormd("test-editormd", {
        width: "90%",
        height: 740,
        path : '../../library/frame/editorMd/lib/',//需要指向editorMd框架目录下的lib文件夹
        theme : "dark",
        previewTheme : "dark",
        editorTheme : "pastel-on-dark",
        markdown : markdown,
        codeFold : true,
        //syncScrolling : false,
        saveHTMLToTextarea : true,// 保存 HTML 到 Textarea
        searchReplace : true,
        //watch : false,//关闭实时预览
        htmlDecode : "style,script,iframe|on*",//开启 HTML 标签解析，为了安全性，默认不开启
        //toolbar  : false,             //关闭工具栏
        //previewCodeHighlight : false, // 关闭预览 HTML 的代码块高亮，默认开启
        emoji : true,
        taskList : true,
        tocm : true,// Using [TOCM]
        tex : true,                   // 开启科学公式TeX语言支持，默认关闭
        flowChart : true,             // 开启流程图支持，默认关闭
        sequenceDiagram : true,       // 开启时序/序列图支持，默认关闭,
        //dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为true
        //dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
        //dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为true
        //dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为0.1
        //dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
        imageUpload : true,
        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL : "./php/upload.php",//上传图像
        onload : function() {
            console.log('onload', this);
            //this.fullscreen();
            //this.unwatch();
            //this.watch().fullscreen();

            //this.setMarkdown("#PHP");
            //this.width("100%");
            //this.height(480);
            //this.resize("100%", 640);
        }
    });
}

$(function() {
    //尝试从sessionStorage读取noteId值，若得到了值，则以得到的值为准。
    let value = sessionStorage.getItem("noteId");
    if (value !== undefined){
        noteId = value;
    }

    //noteId为0时，不从/note/getNoteContentByNoteId接口获取数据，且显示uploadAssembly。
    if (noteId === 0 || noteId === '0'){
        let uploadForm = document.getElementById('uploadAssembly');
        uploadForm.setAttribute("style","display: block");
        structureEditor('#欢迎使用');
    }else {
        //读取指定noteId对应的markdown文件
        $.get('/note/getNoteContentByNoteId', {noteId: noteId}, function(markdown){
            structureEditor(markdown);
        });
    }

    //监听部分按钮的点击事件
    $("#get-html-btn").bind('click', function() {
        alert(testEditor.getHTML());
    });
    $("#watch-btn").bind('click', function() {
        testEditor.watch();
    });
    $("#unwatch-btn").bind('click', function() {
        testEditor.unwatch();
    });
    $("#preview-btn").bind('click', function() {
        testEditor.previewing();
    });
    $("#show-toolbar-btn").bind('click', function() {
        testEditor.showToolbar();
    });
    $("#close-toolbar-btn").bind('click', function() {
        testEditor.hideToolbar();
    });
});

//上传笔记
function uploadNote() {
    let title = $("input[name='title']").val();
    let describe = $("input[name='describe']").val();
    let content = testEditor.getMarkdown();//得到正文内容
    let noteDto = new NoteDto(title,'markdown',describe,content);

    $.post('/note/addNote', noteDto, function (result) {
        if (result === true){
            alert('上传成功');
        }else {
            alert('上传失败');
        }
    });
}