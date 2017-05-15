var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');

require('./index.css');

import SideBar from './sidebar.jsx';
import TopBar from './topbar.jsx';
import YarmWorkspace from './yarmWorkspace.jsx'


class App extends React.Component {
  render() {
    var nav_components = [{label:'one'},{label:'two'},{label:'three'},{label:'four'}];

    return (
      <div>
        <div>Hello World!</div>
        <div>More text</div>
        <TopBar />
        <SideBar nav_components={nav_components} />
        <YarmWorkspace />
      </div>
    )
  }
}

ReactDOM.render(
  <App />,
  document.getElementById('app')
);
