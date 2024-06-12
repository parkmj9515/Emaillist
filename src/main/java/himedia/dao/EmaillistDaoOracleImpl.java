package himedia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import himedia.vo.EmailVo;

public class EmaillistDaoOracleImpl implements EmaillistDao {
	private String dbuser;
	private String dbpass;

	// 생성자
	public EmaillistDaoOracleImpl(String dbuser, String dbpass) {
		this.dbuser = dbuser;
		this.dbpass = dbpass;
	}

	// 데이터베이스 접속 정보 -> 컨텍스트 파라미터로부터 받아옴
	// Connection 공통 메서드
	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(dburl, dbuser, dbpass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return conn;
	}

	@Override
	public List<EmailVo> getList() { // SELECT
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		List<EmailVo> list = new ArrayList<>();

		try {
			// connection
			conn = getConnection();
			// statement 생성
			stmt = conn.createStatement();
			// 쿼리 전송
			String sql = "SELECT * FROM emaillist ORDER BY created_at DESC";
			// 결과 셋
			rs = stmt.executeQuery(sql);
			// 결과 셋 -> 자바 객체로 전환
			while (rs.next()) { // 뒤쪽 레코드를 받아옴
				// Java 객체로 전환
				Long no = rs.getLong("no");
				String lastName = rs.getString("last_name");
				String firstName = rs.getString("first_name");
				String email = rs.getString("email");
				Date createdAt = rs.getDate("created_at");

				EmailVo vo = new EmailVo(no, lastName, firstName, email, createdAt);

				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 자원 해제
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public boolean insert(EmailVo vo) { // INSERT
		Connection conn = null;
		PreparedStatement pstmt = null;
		int insertedCount = 0;

		try {
			// 커넥션 획득
			conn = getConnection();
			// 실행 계획
			String sql = "INSERT INTO emaillist (no, last_name, first_name, email) "
					+ "values (seq_emaillist_pk.nextval, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			// 데이터 바인딩
			pstmt.setString(1, vo.getLastName());
			pstmt.setString(2, vo.getFirstName());
			pstmt.setString(3, vo.getEmail());
			// 확정된 쿼리 수행
			insertedCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 1 == insertedCount;
	}

	@Override
	public boolean delete(Long no) {
		// TODO Auto-generated method stub
		return false;
	}

}