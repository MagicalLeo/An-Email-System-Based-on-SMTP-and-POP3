import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/Login'
import AdminLogin from '@/pages/AdminLogin'
import Register from '@/pages/Register'
import HomePage from '@/pages/HomePage'
import UserManage from '@/pages/UserManage'
import AddUser from '@/pages/AddUser'
import WriteMail from '@/pages/WriteMail'
import EditUser from '@/pages/EditUser'
import EditMyself from '@/pages/EditMyself'
import MailBox from '@/pages/MailBox'
import CheckMail from '@/pages/CheckMail'
import SaveBox from '@/pages/SaveBox'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path:'/',
      name:'Login',
      component: Login,
    },{
      path:'/AdminLogin',
      name:'AdminLogin',
      component: AdminLogin,
    }
    ,{
      path:'/Register',
      name:'Register',
      component: Register,
    },{
      path:'/HomePage',
      name:'HomePage',
      component: HomePage,
    },{
      path:'/UserManage',
      name:'UserManage',
      component:UserManage
    },{
      path:'/AddUser',
      name:'AddUser',
      component:AddUser
    },{
      path:'/WriteMail',
      name:'WriteMail',
      component:WriteMail
    }
    ,{
      path:'/EditUser',
      name:'EditUser',
      component:EditUser
    },{
      path:'/EditMyself',
      name:'EditMyself',
      component:EditMyself
    },{
      path:'/MailBox',
      name:'MailBox',
      component:MailBox
    },{
      path:'/CheckMail',
      name:'CheckMail',
      component:CheckMail
    },{
      path:'/SaveBox',
      name:'SaveBox',
      component:SaveBox
    }
  ]
})
