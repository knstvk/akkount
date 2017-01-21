module.exports = {
  replacePrefix: '/app-front',
  staticFileGlobs: [
    'index.html',
    'src/**.html',
    'images/*',
    'manifest.json',
    'bower_components/webcomponentsjs/webcomponents-lite.min.js',
    'bower_components/fetch/fetch.js'
  ],
  navigateFallback: 'index.html'
};
