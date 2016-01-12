__author__ = 'Ahmed Aly'

import shutil
import glob
import os
import subprocess

TOMCAT_WEBAPPS_FOLDER = "/usr/share/tomcat8/webapps"

TOMCAT_WEBAPPS_DIR = '/usr/share/tomcat8/webapps'
tomcat = "tomcat8"
tomcat_shutdown_period = 8
tomcat_alive_command = "ps -ef | grep -v grep | grep " + tomcat + " | wc -l"
tomcat_process_id_command = 'ps -ef | grep -v grep | grep ' + tomcat + ' | awk \'{print $2}\''
tomcat_shutdown_command = "sudo service tomcat8 stop"
tomcat_startup_command = "sudo service tomcat8 start"


def clean():
    for fl in glob.glob(TOMCAT_WEBAPPS_DIR + "/*ROOT*"):
        # Do what you want with the file
        print "removing file = ", fl
        os.remove(fl)


def deploy():
    # stop()
    # subprocess.call("mvn clean package -P prod")
    clean()
    shutil.copyfile("target/*.original", TOMCAT_WEBAPPS_DIR + "/ROOT.war")
    # start()


def run():
    deploy()


def is_process_running():
    p_status = True
    t_process = subprocess.Popen(tomcat_alive_command, stdout=subprocess.PIPE, shell=True)
    out, err = t_process.communicate()
    if int(out) < 1:
        p_status = False
    return p_status


def start():
    if is_process_running():
        print "Tomcat process is already running"
    else:
        print "Starting the tomcat"
        subprocess.call(tomcat_startup_command)


#
# def stop():
#     if is_process_running():
#         print "Stopping Tomcat process..."
#         t_pid = subprocess.Popen([tomcat_process_id_command], stdout=subprocess.PIPE, shell=True)
#         out, err = t_pid.communicate()
#         subprocess.Popen(["kill -9 " + out], stdout=subprocess.PIPE, shell=True)
#         print "Tomcat failed to shutdown, so killed with PID " + out
#     print "Tomcat process is not running"
#
#
# def status():
#     if is_process_running():
#         t_pid = subprocess.Popen([tomcat_process_id_command], stdout=subprocess.PIPE, shell=True)
#         out, err = t_pid.communicate()
#         print "Tomcat process is running with PID " + out
#     else:
#         print "Tomcat process is not running"
#

# if len(sys.argv) != 2:
#     print "Missing argument"
#     sys.exit(0)
# else:
#     action = sys.argv[1]

if __name__ == '__main__':
    run()
