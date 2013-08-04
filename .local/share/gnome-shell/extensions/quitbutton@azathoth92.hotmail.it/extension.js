// ICONPATH=~/.icons/$(gconftool-2 --get /desktop/gnome/interface/icon_theme)
// mkdir -p $ICONPATH/scalable/actions
// mkdir -p $ICONPATH/actions/scalable
// ln -s /usr/share/icons/gnome/scalable/actions/system-shutdown-symbolic.svg $ICONPATH/scalable/actions
// ln -s /usr/share/icons/gnome/scalable/actions/system-shutdown-symbolic.svg $ICONPATH/actions/scalable

//const userMenuButton = imports.ui.main.panel._statusArea['userMenu'];
var userMenuButton;
try {
	userMenuButton = imports.ui.main.panel.statusArea['userMenu']
} catch(TypeError) {
	userMenuButton = imports.ui.main.panel._statusArea['userMenu'];
}

const icons = [
			userMenuButton._offlineIcon,
			userMenuButton._availableIcon,
			userMenuButton._busyIcon,
			userMenuButton._invisibleIcon,
			userMenuButton._awayIcon,
			userMenuButton._idleIcon
		];

var old_names = new Array(icons.length);

function init() {	
}

function enable() {
	userMenuButton._name.hide();
	userMenuButton._iconBox.show();
	
	for(var i in icons) {
		old_names[i] = icons[i].get_icon_name();
		icons[i].set_icon_name('system-shutdown-symbolic');
	}
}

function disable() {
	userMenuButton._name.show();
	
	for(var i in icons) {
		icons[i].set_icon_name(old_names[i]);
	}
}
