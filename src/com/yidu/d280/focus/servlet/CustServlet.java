package com.yidu.d280.focus.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.yidu.d280.dao.focus.impl.CustDaoImpl;
import com.yidu.d280.focus.dao.CustDao;
import com.yidu.d280.focus.domain.Cust;

/**
 * Servlet implementation class CustServlet
 */
@WebServlet("/CustServlet")
public class CustServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CustServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	request.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		
		
		String method=request.getParameter("method");
		//System.out.println("method:"+method);
		if("findAll".equals(method)){
			this.findAll(request,response);
		}else if("add".equals(method)){
			this.add(request,response);
		}else if("update".equals(method)){
			this.update(request,response);
		}else if("delete".equals(method)){
			this.delete(request,response);
		}
		
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		//获取来自请中所传递过来的员工编号字符串
		String cidStr=request.getParameter("cidStr");
		//将字符串进行按","(逗号)分割的子字符串数组
		String[] cids=cidStr.split(",");
		
		//创建数据层操作对象
		CustDao custDao=new CustDaoImpl();
		
		//由于是批量删除，有可能失败，此处使用异常处理来实现
		try{
			//使用循环进行批量删除
			for(int i=0;i<cids.length;i++){
				//获取当前子字符串
				String cid=cids[i];
				//执行删除操作
				custDao.deleteById(cid);
			}
			out.print("success");
		}catch(Exception e){
			out.print("failure");
		}
		//关闭输出对象
		out.close();
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		
		//接收来自客户端的数据
		
		String cid=request.getParameter("cid");
		String cname=request.getParameter("cname");
		String sex=request.getParameter("sex");
		String telphone=request.getParameter("telphone");
		String cardDate=request.getParameter("cardDate");
		double rateMoney=Double.parseDouble(request.getParameter("rateMoney"));
		String eno=request.getParameter("eno");
		
		//数据封装成对象
		Cust cust=new Cust(cid, cname, sex, telphone, cardDate, rateMoney, eno);
		
		//调用数据层对象执行添加方法
		CustDao empDao=new CustDaoImpl();
		int rows=empDao.update(cust);
		//判断修改是否成功
		if(rows>0){
			out.print("success");
		}else{
			out.print("failure");
		}
		
		out.close();
	}

	/**
	 * 增加
	 * @param request
	 * @param response
	 */
	private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		//System.out.println("1111");
		//接收来自客户端的数据
		String cid=request.getParameter("cid");
		String cname=request.getParameter("cname");
		String sex=request.getParameter("sex");
		String telphone=request.getParameter("telphone");
		String cardDate=request.getParameter("cardDate");
		double rateMoney=Double.parseDouble(request.getParameter("rateMoney"));
		String eno=request.getParameter("eno");
		
		//数据封装成对象
		Cust cust=new Cust(cid, cname, sex, telphone, cardDate, rateMoney, eno);
		//System.out.println("2222");
		//调用数据层对象执行添加方法
		CustDao custDao=new CustDaoImpl();
		int rows=custDao.add(cust);
		//判断添加是否成功
		if(rows>0){
			out.print("success");
			//System.out.println("3333");
		}else{
			out.print("failure");
			//System.out.println("4444");
		}
		
		out.close();
	}

	private void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		/*
		//得到来自请求中的条件数据
		String empName=request.getParameter("empName");
		String job=request.getParameter("job");
		String deptno=request.getParameter("deptno");
		
		//生成查询条件
		//select * from 表名 where 条件 limit n1,n2 
		//where 字段名=? and 字段名=? and 字段名=?
		String condition=" where 1=1 ";
		//对条件数据进行判断并组合成查询条件
		if(empName!=null && !empName.equals("")){
			condition=condition+"and empName like '%"+empName+"%' ";
		}
		
		if(job!=null && !job.equals("")){
			condition=condition+"and job like '%"+job+"%' ";
		}
		
		if(deptno!=null && !deptno.equals("")){
			//由于deptno是int类型，首先转换类型
			int dept=Integer.parseInt(deptno);
			//添加到条件中
			condition=condition+"and deptno="+dept+" ";
		}
		
		System.out.println("condition="+condition);
		*/
		//接收来自客户端的datagrid组件的传递过来的page和rows参数
		int rows=Integer.parseInt(request.getParameter("rows"));
		int page=Integer.parseInt(request.getParameter("page"));
		
		//调用数据层执行分页查询
		CustDao custDao=new CustDaoImpl();
		//获得当前页的数据集合
		List<Cust> custList=custDao.findByPage(rows, page);
		//查询出emp表的总记录数
		int totalRows=custDao.count();
		
		//A方式：
		/*
		//将数据集合转换成JSON数据格式
		String jsonStr=JSONArray.fromObject(empList).toString();
		//生成格式化数据
		String json="{\"total\":"+totalRows+",\"rows\":"+jsonStr+"}";
		*/
		
		//B:方式
		//定义映射集合对象
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("total", totalRows);
		mapData.put("rows", custList);
		//定义Gson对象
		Gson gson = new Gson();
		//通过Gson对象将Map集合转换成json数据格式
		String jsonData = gson.toJson(mapData);
		//将json数据输出到客户端
		out.println(jsonData);
		//System.out.println(jsonData);
		out.close();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
