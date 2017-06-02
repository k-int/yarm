var path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
		entry: './app/index.jsx',
		output: {
			path: path.resolve(__dirname, 'dist'),
			filename: 'index_bundle.js',
			publicPath: '/',
      sourceMapFilename: 'index_bundle.js.map'
		},
		module: {
			rules: [
				{ exclude: /node_modules/, test: /\.jsx(x?)$/, use: 'babel-loader' },
				{ test: /\.css$/, use: [ 'style-loader', 'css-loader' ]}
			]
		},
		devServer:{
			historyApiFallback:true
		},
		plugins: [
			new HtmlWebpackPlugin({
				template: 'app/index.html'
			})
		]
};
