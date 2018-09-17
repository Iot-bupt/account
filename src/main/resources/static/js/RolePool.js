		var app = new Vue({
  			el: '#app',
  			data: {
   				roleInfo:[{name:"超级管理员",ID:1},
   							{name:"管理员",ID:2},
   							{name:"租户",ID:3},
   							{name:"用户",ID:4}],
          activeItem:''
  			},
        methods:{
          selected:function(item){
              this.activeItem=item
          } 
        },
        created:function(){
          this.activeItem=this.roleInfo[0]
        }
		})
