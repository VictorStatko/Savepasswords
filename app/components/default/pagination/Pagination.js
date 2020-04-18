import React, {Component} from 'react';
import {default as PaginationComponent} from 'rc-pagination';
import 'rc-pagination/assets/index.css';
import * as PropTypes from "prop-types";

class Pagination extends Component {

    render() {
        const {total, pageSize, current, onChange} = this.props;

        return (
            <PaginationComponent total={total} pageSize={pageSize} current={current} onChange={onChange}
                                 hideOnSinglePage showLessItems={true} showTitle={false} showPrevNextJumpers={false}/>
        );
    }
}


export default Pagination;

Pagination.propTypes = {
    total: PropTypes.number.isRequired,
    pageSize: PropTypes.number.isRequired,
    current: PropTypes.number.isRequired,
    onChange: PropTypes.func.isRequired
};