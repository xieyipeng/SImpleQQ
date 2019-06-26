from os import mkdir
from os.path import exists, join
import shutil


def delete_dir(path):
	shutil.rmtree(path)


def make_dir(path, ignore_if_exists=True):
	if ignore_if_exists:
		if not exists(path=path):
			mkdir(path=path)
	else:
		mkdir(path=path)


def get_path(dir, file):
	if dir is not None and dir!='':
		return join(dir, file)
	else:
		return str(file)

