# -*- coding: utf-8 -*-
import sys
import time
import MySQLdb
from flask import Flask, g, render_template, request

reload(sys)
sys.setdefaultencoding('utf-8')

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

def get_db():
  db = getattr(g, '_db', None)
  if db is None:
    hostname = '127.0.0.1'
    user = 'xxx'
    password = 'xxx'
    database = 'Memory'
    db = g._db = MySQLdb.connect(hostname, user, password, database, charset='utf8')
  return db

def initDB():
  with app.app_context():
    db = get_db()
    with app.open_resource('init.sql', mode='r') as f:
      c = db.cursor()
      c.execute(f.read())
      db.commit()

@app.before_request
def before_request():
  if not hasattr(g, '_db'):
    g._db = get_db()

@app.teardown_request
def teardown_request(exception):
  if hasattr(g, '_db'):
    g._db.close()

@app.route('/share', methods = ['POST'])
def share():
  try:
    task = request.form
    uuid = task['uuid']
    cursor = g._db.cursor()
    cursor.execute("select uuid from Task where uuid = '%s'" % uuid.strip())
    rv = cursor.fetchone()
    if rv != None:
      cursor.execute("delete from Task where uuid = '%s'" % uuid)
      g._db.commit()
    cursor.execute("""INSERT INTO Task VALUES ('%s', '%s', %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s');""" % 
      (task.get('uuid'), task.get('topic'), task.get('ddl'), task.get('detail', ''),
       task.get('isTimeRemind', 'false'), task.get('isPlaceRemind', 'false'),
      task.get('placeUid', ''), task.get('placeDescription', ''), task.get('longitude', '0.0'), task.get('latitude', '0.0')))
    g._db.commit()
  except Exception as e:
    print e
    return '', 500
  return '', 200

@app.route('/share')
def getShare():
  try:
    uuid = request.args.get('uuid', '')
    print uuid
    cursor = g._db.cursor()
    cursor.execute("select * from Task where uuid = '%s'" % uuid.strip())
    c = cursor.fetchone()
    if c == None:
      return u"该备忘不存在"
  except Exception as e:
    print e
    return u"该备忘不存在"

  task = '''uuid=%s&topic=%s&ddl=%s&detail=%s&isTimeRemind=%s&isPlaceRemind=%s&placeUid=%s&placeDescription=%s&longitude=%s&latitude=%s''' % c
  return render_template('import.html', task = task)

@app.route('/download')
def download():
  return render_template('download.html')

if __name__ == '__main__':
  #  initDB()
  app.run(host = "0.0.0.0", debug = True)
