vim.cmd [[
augroup textwidth_user_config
autocmd!
autocmd BufRead,BufNewFile *.py,*.java setlocal textwidth=100
augroup end
]]
