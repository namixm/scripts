#!/usr/bin/env filebot -script


// require mkvtoolnix and mp4v2 tools
execute 'mkvpropedit', '--version'
execute 'mp4tags',     '--version'


void tag(f, m) {
	switch(f.extension) {
		case ~/mkv/:
			execute 'mkvpropedit', '--verbose', f, '--edit', 'info', '--set', "title=$m"
			return 
		case ~ /mp4|m4v/:
			execute 'mp4tags', '-song', m, f
			return
		default:
			log.warning "[TAGS NOT SUPPORTED] $f"
			return
	}
}


args.getFiles{ it.video }.each{ f ->
	def m = f.metadata
	if (m) {
		log.config "[TAG] Write [$m] to [$f]"
		tag(f, m)
	} else {
		log.finest "[XATTR NOT FOUND] $f"
	}
}