from os.path import basename


def get_basename(path):
	if path == '':
		return ''

	result = basename(path)
	while result == '':
		path = path[:-1]
		result = basename(path)

	return result