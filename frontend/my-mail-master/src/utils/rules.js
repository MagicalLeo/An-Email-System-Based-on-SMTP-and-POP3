const Rules = {
  checkNumber(message) {
    return (rule, value, callback) => {
      if (!value) {
        callback(new Error(message));
      } else if (!/^[0-9]*$/.test(value)) {
        callback(new Error("请输入数字"))
      } else if (!/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error("请输入11位手机号"));
      }
      callback();
    }
  },
  checkIllegal(message) {
    return (rule, value, callback) => {
      if (!value) {
        callback(new Error(message));
      } else if (/[\u4e00-\u9fa5]/.test(value)) { //中文
        callback(new Error("包含非法字符"))
      } else if (/[@#$%^&*]/.test(value)) {
        callback(new Error("包含非法字符"))
      } else if (!/^.{4,13}$/.test(value)) {
        callback(new Error("账号长度为4-13位"));
      }
      callback();
    }
  },checkIllegal2(message) {
    return (rule, value, callback) => {
      if (!value) {
        callback(new Error(message));
      } else if (/[\u4e00-\u9fa5]/.test(value)) { //中文
        callback(new Error("包含非法字符"))
      } else if (/[#$%^&*]/.test(value)) {
        callback(new Error("包含非法字符"))
      }
      callback();
    }
  },
  passwordCheck() {
    return (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入新密码"));
      } else if (!/[A-Z]/.test(value)) {
        callback(new Error("密码需包含大写英文字母"));
      } else if (!/[a-z]/.test(value)) {
        callback(new Error("密码需包含小写英文字母"));
      } else if (!/[0-9]/.test(value)) {
        callback(new Error("密码需包含数字"));
      } else if (!/^.{8,20}$/.test(value)) {
        callback(new Error("密码长度为8-20位"));
      } else {
        callback();
      }
    }
  }
}

export default Rules;
