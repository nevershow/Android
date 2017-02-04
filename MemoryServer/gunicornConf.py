import os
import gevent.monkey
import multiprocessing

gevent.monkey.patch_all()
debug = True
loglevel = "debug"
bind = "0.0.0.0:23333"
workers = multiprocessing.cpu_count() * 2
worker_class = "gunicorn.workers.ggevent.GeventWorker"
keepalive = 4
threads = 2
