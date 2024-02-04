// x轴滚动封装
class doMove {
  constructor(charts, option, end, length) {
    this.chart = charts
    this.timeOut = null//定时器
    this.end = end//默认初始显示几个
    this.dataLength = length//数据长度
    this.option = option//echarts选项
  }
  autoMove() {
    this.move()
    this.chart.on('mouseover', () => {
      clearInterval(this.timeOut)//鼠标移入
    })
    this.chart.on('mouseout', () => {//鼠标移出
      this.move()
    })
  }
  move() {
    this.timeOut = setInterval(() => {
      if (Number(this.option.dataZoom[0].endValue) === this.dataLength - 1) {
        this.option.dataZoom[0].endValue = this.end;
        this.option.dataZoom[0].startValue = 0;
      } else {
        this.option.dataZoom[0].endValue = this.option.dataZoom[0].endValue + 1;
        this.option.dataZoom[0].startValue = this.option.dataZoom[0].startValue + 1;
      }
      this.chart.setOption(this.option)
    }, 3000);
  }
  destory() {
    clearInterval(this.timeOut)
    this.chart = null
  }

}
export default doMove