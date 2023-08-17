export default function loadBMap() {
  return new Promise(function (resolve, reject) {
    if (typeof window.BMap !== 'undefined') {
      resolve(window.BMap)
      return true
    }
    window.onBMapCallback = function () {
      resolve(window.BMap)
    }
    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src =
      'https://api.map.baidu.com/api?v=3.0&ak=' + 'Y7szrgNy6ZKO4py71vIIfPVoP139Ge8M' + '&callback=onBMapCallback'
    script.onerror = reject
    document.head.appendChild(script)
  })
}