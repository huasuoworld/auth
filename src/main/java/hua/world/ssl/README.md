keytool -genkey -keystore E:\db\keystore\server.jks -storepass 123456 -keyalg RSA -keypass 123456
keytool -export -keystore E:\db\keystore\server.jks -storepass 123456 -file E:\db\keystore\server.cer 
keytool -import -keystore E:\db\keystore\server.jks -storepass 123456 -file E:\db\keystore\server.cer

keytool -genkey -keystore c:\sean_app\client.jks -storepass 1234sp -keyalg RSA -keypass 1234kp
keytool -export -keystore c:\sean_app\client.jks -storepass 1234sp -file c:\sean_app\client.cer
keytool -import -keystore c:\sean_app\serverTrust.jks -storepass 1234sp -file c:\sean_app\client.cer 