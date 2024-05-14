package com.jovialcode.parser.parsing;

import java.io.Serializable;
import java.util.List;

public interface Parsing extends Serializable {
    public List<ParsingResult>  parse(String page, List<ParsingRule> parsingRules);
}
