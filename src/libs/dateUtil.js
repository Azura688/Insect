let dateUtil = {};

/* 
** 格式化日期
** 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
** 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
** date: Date类型的日期
** fmt: 格式化模板，如yy-MM-dd
** 返回：格式化后的日期字符串
** 例子：
** (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
** (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
*/
dateUtil.format = function(date, fmt){
  var o = {
    "M+": date.getMonth() + 1, //月份
    "d+": date.getDate(), //日
    "h+": date.getHours(), //小时
    "m+": date.getMinutes(), //分
    "s+": date.getSeconds(), //秒
    "q+": Math.floor((date.getMonth() + 3) / 3), //季度
    S: date.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(
      RegExp.$1,
      (date.getFullYear() + "").substr(4 - RegExp.$1.length)
    );
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
      fmt = fmt.replace(
        RegExp.$1,
        RegExp.$1.length == 1
          ? o[k]
          : ("00" + o[k]).substr(("" + o[k]).length)
      );
  return fmt;
}

// 根据开始日期、结束日期以及间隔得到一个格式化后的日期连续的日期字符串数组
dateUtil.getDateArray = function(start, end, distance, fmt){
  let arr = [];
  let d1 = new Date(start);
  let d2 = new Date(end);
  // console.log(d1, d2);
  while (d1 <= d2) {
    arr.push(dateUtil.format(d1,fmt));
    d1.setTime(d1.getTime() + distance);
  }
  // console.log(arr);
  return arr;
}
dateUtil.getHourArray = function(start, end, distance, fmt){
  let arr = [];
  let d1 = start;
  let d2 = end;
  // console.log(d1, d2);
  while (d1 <= d2) {
    arr.push(dateUtil.format(new Date(d1),fmt));
    d1 = d1 + distance;
  }
  // console.log(arr);
  return arr;
}

export default dateUtil;
