var app = new Vue({
    el: '#app',
    data: {
        tenantInfo:[],
        activeItem: '',
        columns: [
            {
                label: 'ID',
                prop: 'id',
                width:50
            },
            {
                label: '用户名',
                prop: 'name'
            },
            {
                label: '权限',
                prop: 'authority'
            },
            {
                label: '邮箱',
                prop: 'email'
            },
            {
                label: '联系电话',
                prop: 'phone'
            }],
        tableData: [],
    },
    methods:{
        selected: function (item) {
            var vm=this
            vm.activeItem = item
            vm.getUserByTenant()
        },
        getAllTenants: function () {
            var vm = this
            //获取所有租户
            axios.get("/api/v1/account/tenants?limit=20&page=0").then(function (response) {
                vm.tenantInfo = response.data
                vm.activeItem = vm.tenantInfo[0]
                vm.getUserByTenant()
            }).catch(function (error) {
                console.log(error);
            })
        },
        getUserByTenant:function () {
            var vm = this

            //根据role_id获取role
            axios.get("/api/v1/account/tenant/user", {
                params: {
                    tenantId: vm.activeItem.id
                }
            }).then(function (response) {
                vm.tableData = response.data
            }).catch(function (error) {
                console.log(error);
            })
        },
    },
    created: function () {
        var vm = this
        vm.getAllTenants()
    }
})