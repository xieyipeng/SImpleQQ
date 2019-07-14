from .pickle import pickle as _pickle
from .pickle import unpickle as _unpickle
from .individual_functions import *


class Path:
	def __init__(self, string, show_size=False):
		self._string = string
		self._size = None
		self._show_size = show_size

	def __str__(self):
		return self.__repr__()

	def __repr__(self):
		string = self.name_and_extension
		if self._show_size:
			size, unit = self.get_size()
			return f'{self.type}: {string} - {round(size, 3)} {unit}'
		else:
			return f'{self.type}: {string}'

	def __getstate__(self):
		return {
			'string': self._string,
			'size': self._size,
			'show_size': self._show_size
		}

	def __setstate__(self, state):
		self._string = state['string']
		self._size = state['size']
		self._show_size = state['show_size']

	def get_absolute(self):
		return get_absolute_path(path=self.path)

	@property
	def absolute(self):
		return self.get_absolute()

	@property
	def absolute_path(self):
		return self.get_absolute()

	@classmethod
	def get_current_directory(cls, show_size=False):
		return cls(string='.', show_size=show_size)

	@property
	def parent_directory(self):
		result = self + '..'
		if result.absolute_path != self.absolute_path:
			result._show_absolute_path = True
			return result
		else:
			return None

	def __parents__(self):
		if self.parent_directory is not None:
			return [self.parent_directory]
		else:
			return []

	def __children__(self):
		if self.is_directory():
			return self.list()
		else:
			return []

	def __hash__(self):
		return self.absolute_path

	@property
	def string(self):
		return self._string

	@property
	def path(self):
		return self.string

	@property
	def value(self):
		return self.string

	@property
	def name_and_extension(self):
		return get_basename(path=self.absolute_path)

	full_name = name_and_extension

	@property
	def extension(self):
		extension_with_dot = os.path.splitext(self.path)[1]
		if len(extension_with_dot) > 0:
			return extension_with_dot[1:]
		else:
			return extension_with_dot

	@property
	def name(self):
		return self.name_and_extension.rsplit('.', 1)[0]

	def get_size_bytes(self):
		if self.is_file():
			return get_file_size_bytes(path=self.path)
		else:
			return sum([x.size_bytes for x in self.list()])

	@property
	def size_bytes(self):
		if self._size is None:
			self._size = self.get_size_bytes()
		return self._size

	def get_size_kb(self, binary=True):
		if binary:
			return self.size_bytes/(2**10)
		else:
			return self.size_bytes/1e3

	def get_size_mb(self, binary=True):
		if binary:
			return self.size_bytes/(2**20)
		else:
			return self.size_bytes/1e6

	def get_size_gb(self, binary=True):
		if binary:
			return self.size_bytes/(2**30)
		else:
			return self.size_bytes/1e6

	def get_size(self, binary=True):
		main_unit = 'B' if binary else 'b'
		if self.size_bytes <= 1e3:
			return self.size_bytes, 'B'
		elif self.size_bytes <= 1e6:
			return self.get_size_kb(binary=binary), 'K' + main_unit
		elif self.size_bytes <= 1e9:
			return self.get_size_mb(binary=binary), 'M' + main_unit
		else:
			return self.get_size_gb(binary=binary), 'G' + main_unit

	def exists(self):
		return path_exists(path=self.path)

	def is_file(self):
		return path_is_file(path=self.path)

	def is_directory(self):
		return not self.is_file()

	@property
	def type(self):
		if self.is_file():
			return 'file'
		else:
			return 'directory'

	def __add__(self, other):
		if isinstance(other, str):
			other_string = other
		elif isinstance(other, self.__class__):
			other_string = other.string
		else:
			raise TypeError(f'{other} is a {type(other)} but it should either be string or {self.__class__}')
		self_string = '' if self.string == '.' else self.string

		return self.__class__(
			string=os.path.join(self_string, other_string),
			show_size=self._show_size
		)

	def _sort_key(self):
		return self.type, self.get_absolute()

	def __lt__(self, other):
		return self._sort_key() < other._sort_key()

	def __gt__(self, other):
		return self._sort_key() > other._sort_key()

	def __le__(self, other):
		return self._sort_key() <= other._sort_key()

	def __ge__(self, other):
		return self._sort_key() >= other._sort_key()

	def __eq__(self, other):
		return self._sort_key() == other._sort_key()

	def __ne__(self, other):
		return self._sort_key() != other._sort_key()

	def list(self, show_size=None):
		if show_size is not None:
			self._show_size = show_size
		result = [self+x for x in list_directory(path=self.path)]
		result = [x for x in result if x.name != '']
		result.sort()
		return result

	@property
	def directories(self):
		return [x for x in self.list() if x.is_directory()]

	@property
	def files(self):
		return [x for x in self.list() if x.is_file()]

	def get(self, full_name):
		return [x for x in self.list() if x.full_name == full_name][0]

	def make_directory(self, name=None, ignore_if_exists=True):
		if name:
			path = (self+name).path
		else:
			path = self.path

		make_dir(path=path, ignore_if_exists=ignore_if_exists)
		return self

	def make_parent_directory(self, ignore_if_exists=True):
		if self.parent_directory:
			self.parent_directory.make_directory(ignore_if_exists=ignore_if_exists)
		return self.parent_directory

	def delete(self, name=None):
		if name:
			to_delete = self + name
		else:
			to_delete = self
		delete(path=to_delete.path)

	def delete_directory(self, name):
		delete_dir(path=(self+name).path)

	def save(self, obj, method='pickle', mode='wb', echo=0):
		_pickle(path=self.path, obj=obj, method=method, mode=mode, echo=echo)

	def load(self, method='pickle', mode='rb', echo=0):
		return _unpickle(path=self.path, method=method, mode=mode, echo=echo)

	# aliases
	ls = list
	dir = list
	md = make_directory
	make_dir = make_directory
	del_dir = delete_directory
	delete_dir = delete_directory
	pickle = save
	unpickle = load
