var React = require('react');
var ReactDOM = require('react-dom');
var PropTypes = require('prop-types');
import { ScrollView, Box, Page, VBox, Flex } from 'react-layout-components';


export default class PublicHome extends React.Component {

  constructor (props) {
    super(props);
    this.state={
    };
    // this.updateSelectedComponent=this.updateSelectedComponent.bind(this);
  }

  // updateSelectedComponent(comp){
  //   this.setState(function(){
  //     return {
  //       selectedComponent:comp
  //     }
  //   });
  // }

  render() {

    return (
      <Page>
        <Box fit>
          <Box>
            Public Nav
          </Box>
          <Box>
            Public Body
          </Box>
        </Box>
      </Page>
    )
  }
}

PublicHome.propTypes={
};

module.exports = PublicHome;
