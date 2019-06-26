import os
import os.path
from send2trash import send2trash
import shutil


def get_basename(path):
	if path == '':
		return ''

	result = os.path.basename(path)
	while result == '':
		path = path[:-1]
		result = os.path.basename(path)

	return result


def get_absolute_path(path):
	return os.path.abspath(path)


def get_file_size_bytes(path):
	if path_is_file(path=path):
		return os.path.getsize(path)
	else:
		raise FileNotFoundError(f'The file "{path}" does not exist!')


def path_exists(path):
	return os.path.exists(path)


def path_is_file(path):
	if path_exists(path=path):
		return os.path.isfile(path)
	else:
		raise ValueError(f'The path "{path}" does not exist!')


def path_is_directory(path):
	return not path_is_file(path=path)


def delete(path):

	# path should exist
	if path_exists(path=path):
		send2trash(path=path)
	else:
		raise FileNotFoundError(f'The path "{path}" does not exist!')

	# the path shouldn't exist after deletion
	if path_exists(path=path):
		raise FileExistsError(f'Failed to delete the path "{path}"')


def delete_dir(path):
	shutil.rmtree(path)


def make_dir(path, ignore_if_exists=True):
	if ignore_if_exists:
		if not path_exists(path=path):
			os.mkdir(path=path)
	else:
		os.mkdir(path=path)


def get_path(directory, file):
	if directory is not None and directory != '':
		return os.path.join(directory, file)
	else:
		return str(file)


def list_directory(path):
	if path_is_directory(path=path):
		return os.listdir(path=path)
	else:
		raise ValueError(f'The path "{path}" is not a directory!')
