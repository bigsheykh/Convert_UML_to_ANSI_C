void String::set_chars(char* characters, int size_of_string)
{
	this->all = characters;
	size = size_of_string;
}

char* String::get_chars()
{
	return all;
}

