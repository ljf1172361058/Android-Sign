<?php
/**
 * 判断今日是否签退过接口
 */
if(is_array($_GET)&&count($_GET)>0){//判断是否有Get参数
	if(isset($_GET['user_id'])){//是否设置user_id参数
		$user_id=$_GET['user_id'];
		include 'DBHelper.php';
		try {
			$pdo = new PDO($dsn, $user, $pwd); //初始化一个PDO对象
			$pdo->query("set character_set_results=utf8");//设置编码
			$pdoStatement=$pdo->query("SELECT count(*) FROM sign_in WHERE user_id='$user_id' AND DATEDIFF(user_signOut_time,NOW())=0");
			/*此处省略了if判断是否有结果集的操作 因为count(*)函数在没有查询出记录是也会返回0*/
			$rows=$pdoStatement->fetch(PDO::FETCH_NUM);//返回一个索引为以0开始的结果集列号的数组
			$rowCount = $rows[0];
			//返回结果集中的列数
			if($rowCount>0){//今日签退过
				 echo "yes";
			}
			else{//今日未签退过
				 echo "no";
			}
			$pdo = null;
		} catch (PDOException $e) {
			die ("Error!: " . $e->getMessage() . "<br/>");
		}
	}
	else{
		print "{";
		print "\"result\":\"参数不存在\"";
		print "}";
	}
}
else{
	print "{";
	print "\"result\":\"请求参数不存在\"";
	print "}";
}
?>