var React = require('react');
var ReactDOM = require('react-dom');
require('./index.css');
import SideBar from './sidebar.jsx';
import TopBar from './topbar.jsx';


class App extends React.Component {
  render() {
    return (
      <div>
        <div>Hello World!</div>
        <div>More text</div>
        <TopBar />
      </div>
    )
  }
}

ReactDOM.render(
  <App />,
  document.getElementById('app')
);
