/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.sql;

import java.sql.Types;

import com.mysema.query.types.Ops;

/**
 * HSQLDBTemplates is an SQL dialect for HSQLDB
 *
 * @author tiwe
 *
 */
public class HSQLDBTemplates extends SQLTemplates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new HSQLDBTemplates(escape, quote);
            }
        };
    }

    public HSQLDBTemplates() {
        this('\\', false);
    }

    public HSQLDBTemplates(boolean quote) {
        this('\\', quote);
    }

    public HSQLDBTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setLimitRequired(true);
        setAutoIncrement(" identity");
        setDefaultValues("\ndefault values");
        setFunctionJoinsWrapped(true);
        add(Ops.TRIM, "trim(both from {0})");
        add(Ops.NEGATE, "{0} * -1", 7);

        add(SQLOps.NEXTVAL, "next value for {0s}");

        add(Ops.MathOps.ROUND, "round({0},0)");
        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "(log({0}) / log({1}))");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0} * 2) - 1) / (exp({0} * 2) + 1)");

        add(Ops.DateTimeOps.WEEK, "extract(week_of_year from {0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "(extract(year from {0}) * 100 + extract(week_of_year from {0}))");

        add(Ops.DateTimeOps.ADD_YEARS, "dateadd('yy', {1s}, {0})");
        add(Ops.DateTimeOps.ADD_MONTHS, "dateadd('mm', {1s}, {0})");
        add(Ops.DateTimeOps.ADD_WEEKS, "dateadd('week', {1s}, {0})");
        add(Ops.DateTimeOps.ADD_DAYS, "dateadd('dd', {1s}, {0})");
        add(Ops.DateTimeOps.ADD_HOURS, "dateadd('hh', {1s}, {0})");
        add(Ops.DateTimeOps.ADD_MINUTES, "dateadd('mi', {1s}, {0})");
        add(Ops.DateTimeOps.ADD_SECONDS, "dateadd('ss', {1s}, {0})");

        add(Ops.DateTimeOps.DIFF_YEARS, "datediff('yy', {0}, {1})");
        add(Ops.DateTimeOps.DIFF_MONTHS, "datediff('mm', {0}, {1})");
        add(Ops.DateTimeOps.DIFF_WEEKS, "trunc(datediff('dd', {0}, {1}) / 7)");
        add(Ops.DateTimeOps.DIFF_DAYS, "datediff('dd', {0}, {1})");
        add(Ops.DateTimeOps.DIFF_HOURS, "datediff('hh', {0}, {1})");
        add(Ops.DateTimeOps.DIFF_MINUTES, "datediff('mi', {0}, {1})");
        add(Ops.DateTimeOps.DIFF_SECONDS, "datediff('ss', {0}, {1})");

        add(Ops.DateTimeOps.DATE, "convert({0}, date)");

        addTypeNameToCode("character", Types.CHAR, true);
        addTypeNameToCode("float", Types.DOUBLE, true);
        addTypeNameToCode("real", Types.DOUBLE);
        addTypeNameToCode("nvarchar", Types.VARCHAR);
    }

    @Override
    public String getCastTypeNameForCode(int code) {
        if (code == Types.VARCHAR) {
            return "varchar(10)";
        } else {
            return super.getCastTypeNameForCode(code);
        }
    }

}
