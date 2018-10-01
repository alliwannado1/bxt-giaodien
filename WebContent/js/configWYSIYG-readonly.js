CKEDITOR.editorConfig = function(config) {
	config.resize_enabled = false;
	config.toolbar = 'Complex';
	config.toolbar = 'Simple';
	config.toolbar_Simple = [ [ 'Bold', 'Italic', '-', 'NumberedList',
			'BulletedList', '-', 'Link', 'Unlink', '-', 'About' ] ];
	config.toolbar_Complex = [ ['Styles', 'Format', 'Font',
	                 			'FontSize'] ];
	//config.readOnly = true;
};