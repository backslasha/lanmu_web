package lanmu.entity.api.base;

import com.google.gson.annotations.Expose;

import java.util.List;


public class PageModel<T> {

    @Expose
    private List<T> result;

    @Expose
    private int page;
    @Expose
    private int total;
    @Expose
    private boolean end;

    public PageModel(List<T> result, int page, int total, boolean end) {
        this.result = result;
        this.page = page;
        this.total = total;
        this.end = end;
    }

    public PageModel() {

    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "{" + "\"result\":" +
                result +
                ",\"page\":" +
                page +
                '}';
    }
}
