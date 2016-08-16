<?php
/**
 * 签退接口
 */
if(is_array($_POST)&&count($_POST)>0){//判断是否有Get参数
	if(isset($_POST['user_id'])){//是否设置user_id参数
		$user_id=$_POST['user_id'];
		$address=$_POST['address'];
		include 'DBHelper.php';
		try {
			$pdo = new PDO($dsn, $user, $pwd); //初始化一个PDO对象
			$pdo->query("SET NAMES 'utf8'");//设置编码
			$pdoStatement=$pdo->prepare("UPDATE sign_in SET user_signOut_time=NOW(),user_signOut_address=? WHERE user_id=?");//问号占位符
			$pdoStatement->bindParam(1,$address,PDO::PARAM_STR);
			$pdoStatement->bindParam(2,$user_id,PDO::PARAM_INT);
			//执行预处理过的语句
			if($pdoStatement->execute()){//签退成功
				echo "{\"isSucceed\":true,\"groupName\":\"签退\",\"childList\":[{\"address\":\"$address\"}]}";
			}
			else{//签退失败
				echo '{"isSucceed":false}';
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