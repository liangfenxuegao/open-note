//入口函数
$(function(){
    //获取当前登录用户的所有笔记
    $.post('/note/getNoteOfCurrentUser',function (data) {
        vm.noteList = data;
    });
});

//note预览组件
Vue.component('preview-note',{
    props: ['note'],//接受单个note
    template: `
        <div class="col-md-4">
            <div class="card mb-4 shadow-sm">
                <svg class="bd-placeholder-img card-img-top" width="100%" height="225"
                     xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false"
                     role="img" aria-label="Placeholder: Thumbnail"><title>缩略图</title>
                    <rect width="100%" height="100%" fill="#55595c"/>
                    <text x="50%" y="50%" fill="#eceeef" dy=".3em">{{note.title}}</text>
                </svg>
                <div class="card-body">
                    <p class="card-text">{{note.describe}}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="btn-group">
                            <!--使用$emit抛出note.id的值-->
                            <button @click="$emit('select-note-id',note.id)" type="button" class="btn btn-sm btn-outline-secondary">查看</button>
                        </div>
                        <small class="text-muted">{{note.date}}</small>
                    </div>
                </div>
            </div>
        </div>
    `
})

let vm = new Vue({
    el: '#bodyMain',
    data: {
        //笔记列表
        noteList: [
            {id:0, title:'', describe: '', userId:0, fileType:'', date: ''}
        ]
    },
    methods: {
        //向sessionStorage存储noteId值
        saveNoteIdToSession: function (noteId) {
            sessionStorage.setItem('noteId', noteId);
            window.location.assign('noteContent.html');
        }
    }
})
