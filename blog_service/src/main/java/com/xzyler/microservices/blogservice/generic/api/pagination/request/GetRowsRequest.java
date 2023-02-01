package com.xzyler.microservices.blogservice.generic.api.pagination.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@Data
@Valid
public class GetRowsRequest implements Serializable {

    @JsonProperty("pq_curpage")
    @Min(value = 1, message = "Page cannot be less than ")
    private int pq_curpage = 1;

    @JsonProperty("pq_rpp")
    @Min(value = 1, message = "Request Per Page cannot be less than ")
    private int pq_rpp = 10;

    private String pq_filter;
    @JsonProperty("pq_sort")
    private String pq_sort;

    private String filters;

    //not used
    // row group columns
    private List<ColumnVO> rowGroupCols;

    // value columns
    private List<ColumnVO> valueCols;

    // pivot columns
    private List<ColumnVO> pivotCols;

    // true if pivot mode is one, otherwise false
    private boolean pivotMode;

    // what groups the user is viewing
    private List<String> groupKeys;
    //not used


    // if sorting, what the sort model is
    private List<SortModel> sortModel;

    public GetRowsRequest() {
        this.rowGroupCols = emptyList();
        this.valueCols = emptyList();
        this.pivotCols = emptyList();
        this.groupKeys = emptyList();
        this.sortModel = emptyList();
    }

    public int getPq_curpage() {
        if (pq_curpage == 0)
            return pq_curpage;
        return pq_curpage - 1;
    }
}
