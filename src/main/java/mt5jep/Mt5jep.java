package mt5jep;
import java.util.LinkedHashMap;
import java.util.List;
import jep.Interpreter;
import jep.JepException;

public class Mt5jep{
    Interpreter interp;

    /**
     * initialize the class.
     * will do imports and set some methods to the interpreter.
     * @param interp:
     *              a jep sharedInterpreter, jep interpreter can only keep one instance, by this reaseon
     *              this class requires do you open a jep interpreter beviously this way the same interpreter
     *              can be used by this library and by yours.
     * */
    public void configure(Interpreter interp){
        this.interp = interp;

        this.interp.exec("import MetaTrader5 as mt5");
        this.interp.exec("import pandas as pd");

        // alocate a function to the interpreter to convert a Tuple Of NamedTuple to a list of maps.
        this.interp.exec("""
        def namedTupleT_to_listDict(x):
            result = []
            for i in x:
                result.append(i._asdict())
            return result
        """);
    }

    /**
     * interpret a string to a mt5. the given string will be called placing it after the keyword 'mt5' (mt5.thisString).
     * */
    public int mt5_strInterp(String timeframe){
        this.interp.set("mt5_strInterp_timeframe", timeframe);
        this.interp.exec("mt5_strInterp_result = eval(f'mt5.{mt5_strInterp_timeframe}')");
        return this.interp.getValue("mt5_strInterp_result", Integer.class);
    }

    public boolean initialize(String path){
        return (boolean) this.interp.invoke("mt5.initialize", path);
    }

    public boolean login(int login, String password, String server, int timeout){
        return (boolean) this.interp.invoke("mt5.login", login, password, server, timeout);
    }

    public void shutdown(){
        this.interp.invoke("mt5.shutdown");
    }

    public List<Object> version() throws Exception{
        // ignore these vscode warning, the conversion works, but vscode recommends
        // generics types be parametrized, but i dont know how to parametrize a 
        // collection for unmodifiableRandomAccessList.
        return (List<Object>) this.interp.invoke("mt5.version");
        
    }

    public List<Object> last_error(){
        return (List<Object>) this.interp.invoke("mt5.last_error");
    }

    public LinkedHashMap<String, Object> account_info() throws JepException{
        this.interp.exec("account_info_result = mt5.account_info()._asdict()");
        return this.interp.getValue("account_info_result", LinkedHashMap.class);
    }

    public LinkedHashMap<String, Object> terminal_info() throws JepException{
        this.interp.exec("terminal_info_result = mt5.terminal_info()._asdict()");
        return this.interp.getValue("terminal_info_result", LinkedHashMap.class);
    }

    public int symbols_total(){
        this.interp.exec("symbols_total_result = mt5.symbols_total()");
        return this.interp.getValue("symbols_total_result", Integer.class);
    }

    public List<LinkedHashMap<String, Object>> symbols_get(String group){
        this.interp.set("symbols_get_group", group);
        this.interp.exec("symbols_get_result = namedTupleT_to_listDict(mt5.symbols_get(symbols_get_group))");
        //this.interp.exec("print(mt5.symbols_get(''))");
        return this.interp.getValue("symbols_get_result", List.class);
    }

    public LinkedHashMap<String, Object> symbol_info(String symbol){
        this.interp.set("symbol_info_symbol", symbol);
        this.interp.exec("symbol_info_result = mt5.symbol_info(symbol_info_symbol)._asdict()");
        return this.interp.getValue("symbol_info_result", LinkedHashMap.class);
    }

    public LinkedHashMap<String, Object> symbol_info_tick(String symbol){
        this.interp.set("symbol_info_tick_symbol", symbol);
        this.interp.exec("symbol_info_tick_result = mt5.symbol_info_tick(symbol_info_tick_symbol)._asdict()");
        return this.interp.getValue("symbol_info_tick_result", LinkedHashMap.class);
    }

    public boolean symbol_select(String symbol, boolean enable){
        this.interp.set("symbol_select_symbol", symbol);
        this.interp.set("symbol_select_enable", enable);
        this.interp.exec("symbol_select_result = mt5.symbol_select(symbol_select_symbol, symbol_select_enable)");
        return this.interp.getValue("symbol_select_result", Boolean.class);
    }

    public boolean market_book_add(String symbol){
        this.interp.set("market_book_add_symbol", symbol);
        this.interp.exec("market_book_add_result = mt5.market_book_add(market_book_add_symbol)");
        return this.interp.getValue("market_book_add_result", Boolean.class);
    }

    /**
     * this function is returning a object because seems it is not working, is mql5 fault.
     */
    public Object market_book_get(String symbol){
        this.interp.set("market_book_get_symbol", symbol);
        this.interp.exec("market_book_get_result = mt5.market_book_get(market_book_get_symbol)");
        return this.interp.getValue("market_book_get_result", Object.class);
    }

    public boolean market_book_release(String symbol){
        this.interp.set("market_book_release_symbol", symbol);
        this.interp.exec("market_book_release_result = mt5.market_book_release(market_book_release_symbol)");
        return this.interp.getValue("market_book_release_result", Boolean.class);
    }

    public List<List<Object>> copy_rates_from(String symbol, String timeframe, long date_from, int count){
        this.interp.set("copy_rates_from_symbol", symbol);
        this.interp.set("copy_rates_from_timeframe", this.mt5_strInterp(timeframe));
        this.interp.set("copy_rates_from_date_from", date_from);
        this.interp.set("copy_rates_from_count", count);
        this.interp.exec("""
        copy_rates_from_result = mt5.copy_rates_from(
            copy_rates_from_symbol,
            copy_rates_from_timeframe,
            copy_rates_from_date_from,
            copy_rates_from_count
        ).tolist()
        """);
        return this.interp.getValue("copy_rates_from_result", List.class);
    }

    public List<List<Object>> copy_rates_from_pos(String symbol, String timeframe, long start_pos, int count){
        this.interp.set("copy_rates_from_pos_symbol", symbol);
        this.interp.set("copy_rates_from_pos_timeframe", this.mt5_strInterp(timeframe));
        this.interp.set("copy_rates_from_pos_start_pos", start_pos);
        this.interp.set("copy_rates_from_pos_count", count);
        this.interp.exec("""
        copy_rates_from_pos_result = mt5.copy_rates_from_pos(
            copy_rates_from_pos_symbol,
            copy_rates_from_pos_timeframe,
            copy_rates_from_pos_start_pos,
            copy_rates_from_pos_count
        ).tolist()
        """);
        return this.interp.getValue("copy_rates_from_pos_result", List.class);
    }

    public List<List<Object>> copy_rates_range(String symbol, String timeframe, long date_from, long date_to){
        this.interp.set("copy_rates_range_symbol", symbol);
        this.interp.set("copy_rates_range_timeframe", this.mt5_strInterp(timeframe));
        this.interp.set("copy_rates_range_date_from", date_from);
        this.interp.set("copy_rates_range_date_to", date_to);
        this.interp.exec("""
        copy_rates_range_result = mt5.copy_rates_range(
            copy_rates_range_symbol,
            copy_rates_range_timeframe,
            copy_rates_range_date_from,
            copy_rates_range_date_to
        ).tolist()
        """);
        return this.interp.getValue("copy_rates_range_result", List.class);
    }

    public List<List<Object>> copy_ticks_from(String symbol, long date_from, int count, String flags){
        this.interp.set("copy_ticks_from_symbol", symbol);
        this.interp.set("copy_ticks_from_date_from", date_from);
        this.interp.set("copy_ticks_from_count", count);
        this.interp.set("copy_ticks_from_flags", this.mt5_strInterp(flags));
        this.interp.exec("""
        copy_ticks_from_result = mt5.copy_ticks_from(
            copy_ticks_from_symbol,
            copy_ticks_from_date_from,
            copy_ticks_from_count,
            copy_ticks_from_flags
        ).tolist()
        """);
        return this.interp.getValue("copy_ticks_from_result", List.class);
    }


    public List<List<Object>> copy_ticks_range(String symbol, long date_from, long date_to, String flags){
        this.interp.set("copy_ticks_range_symbol", symbol);
        this.interp.set("copy_ticks_range_date_from", date_from);
        this.interp.set("copy_ticks_range_date_to", date_to);
        this.interp.set("copy_ticks_range_flags", this.mt5_strInterp(flags));
        this.interp.exec("""
        copy_ticks_range_result = mt5.copy_ticks_range(
            copy_ticks_range_symbol,
            copy_ticks_range_date_from,
            copy_ticks_range_date_to,
            copy_ticks_range_flags
        ).tolist()
        """);
        return this.interp.getValue("copy_ticks_range_result", List.class);
    }

    public int orders_total(){
        this.interp.exec("orders_total_result = mt5.orders_total()");
        return this.interp.getValue("orders_total_result", Integer.class);
    }

    /**
     * realy this seems not working is fault of mql5 the mql5 function seems doesn't working.
     * */
    public Object orders_get(String symbol, String group, int... ticket){

        assert ticket.length <= 1 : "ticket must be contains only one value or nothing.";

        this.interp.set("orders_get_symbol", symbol);
        this.interp.set("orders_get_group", group);

        if ( ticket.length == 0 ){
            this.interp.exec("orders_get_result = mt5.orders_get(orders_get_symbol, orders_get_group)");
        }else{
            this.interp.set("orders_get_ticket", ticket[0]);
            this.interp.exec("orders_get_result = mt5.orders_get(orders_get_ticket)");
        }

        return this.interp.getValue("orders_get_result", Object.class);
    }

    public int history_orders_total(long date_from, long date_to){
        this.interp.set("history_orders_total_date_from", date_from);
        this.interp.set("history_orders_total_date_to", date_to);
        this.interp.exec("history_orders_total_result = mt5.history_orders_total(history_orders_total_date_from, history_orders_total_date_to)");
        return this.interp.getValue("history_orders_total_result", Integer.class);
    }

    public Object history_orders_get(long date_from, long date_to, String group){
        this.interp.set("history_orders_get_date_from", date_from);
        this.interp.set("history_orders_get_date_to", date_to);
        this.interp.set("history_orders_get_group", group);
        this.interp.exec("history_orders_get_result = mt5.history_orders_get(history_orders_get_date_from, history_orders_get_date_to, history_orders_get_group)");
        return this.interp.getValue("history_orders_get_result", Object.class);
    }

    public Object history_orders_get(long ticket){
        this.interp.set("history_orders_get_ticket", ticket);
        this.interp.exec("history_orders_get_result = mt5.history_orders_get(history_orders_get_ticket)");
        return this.interp.getValue("history_orders_get_result");
    }

    public Object history_orders_get(String position){
        this.interp.set("history_orders_get_position", mt5_strInterp(position));
        this.interp.exec("history_orders_get_result = mt5.history_orders_get(history_orders_get_position)");
        return this.interp.getValue("history_orders_get_result", Object.class);
    }

    public int history_deals_total(long date_from, long date_to){
        this.interp.set("history_deals_total_date_from", date_from);
        this.interp.set("history_deals_total_date_to", date_to);
        this.interp.exec("history_deals_total_result = mt5.history_deals_total(history_deals_total_date_from, history_deals_total_date_to)");
        return this.interp.getValue("history_deals_total_result", Integer.class);
    }

    public Object history_deals_get(long date_from, long date_to, String group){
        this.interp.set("history_deals_get_date_from", date_from);
        this.interp.set("history_deals_get_date_to", date_to);
        this.interp.set("history_deals_get_group", group);
        this.interp.exec("history_deals_get_result = mt5.history_deals_get(history_deals_get_date_from, history_deals_get_date_to, history_deals_get_group)");
        return this.interp.getValue("history_deals_get_result", Object.class);
    }

    public Object history_deals_get(long ticket){
        this.interp.set("history_deals_get_ticket", ticket);
        this.interp.exec("history_deals_get_result = mt5.history_deals_get(history_deals_get_ticket)");
        return this.interp.getValue("history_deals_get_result");
    }

    public Object history_deals_get(String position){
        this.interp.set("history_deals_get_position", mt5_strInterp(position));
        this.interp.exec("history_deals_get_result = mt5.history_deals_get(history_deals_get_position)");
        return this.interp.getValue("history_deals_get_result", Object.class);
    }
}
