package org.hsgt.core.mapper.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.json.JSONArray;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolesTypeHandler extends BaseTypeHandler<List<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        String s = strings.toString();
        preparedStatement.setString(i, s);
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        JSONArray roles = new JSONArray(resultSet.getString(columnName));
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < roles.length(); i++) {
            ans.add(roles.getString(i));
        }
        return ans;
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int index) throws SQLException {
        JSONArray roles = new JSONArray(resultSet.getString(index));
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < roles.length(); i++) {
            ans.add(roles.getString(i));
        }
        return ans;
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int index) throws SQLException {
        JSONArray roles = new JSONArray(callableStatement.getString(index));
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < roles.length(); i++) {
            ans.add(roles.getString(i));
        }
        return ans;
    }
}
