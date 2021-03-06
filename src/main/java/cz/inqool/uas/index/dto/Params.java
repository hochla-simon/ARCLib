package cz.inqool.uas.index.dto;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.index.query.QueryBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 *  Data transfer object for specification of filtering, sorting and paging.
 *
 *  <p>
 *      Sorting is specified by name of attribute to sort on {@link Params#sort} and ascending or descending order
 *      specified by {@link Params#order}.
 *  </p>
 *  <p>
 *      Paging is specified by number of items to retrieve {@link Params#pageSize} and the page to start
 *      on {@link Params#page}.
 *  </p>
 *  <p>
 *      Filtering is specified by a {@link List} of filters {@link Params#filter}.
 *      {@link cz.inqool.uas.index.IndexedStore} does AND between individual filters.
 *  </p>
 */
@Getter
@Setter
public class Params {
    /**
     * Attribute name to sort on.
     */
    @NotNull
    protected String sort = "created";

    /**
     * Order of sorting.
     *
     * <p>
     *     For possible values see {@link Order}.
     * </p>
     */
    @NotNull
    protected Order order = Order.DESC;

    /**
     * Support for sorting on multiple values, if this is not null, fields sort and order above are ignored.
     */
    protected List<SortSpecification> sorting;

    /**
     * Initial page.
     */
    @NotNull
    protected Integer page = 0;

    /**
     * Number of requested instances.
     *
     * If null specified then paging is disabled and all items are returned.
     */
    protected Integer pageSize = 10;

    /**
     * Logic operation between root filters
     */
    @NotNull
    protected RootFilterOperation operation = RootFilterOperation.AND;

    /**
     * Filter conditions.
     */
    @Valid
    protected List<Filter> filter = new ArrayList<>();

    /**
     * Internal filter conditions.
     *
     * Used for additional complicated filters added on backend.
     */
    protected QueryBuilder internalFilter;
}
