vim.cmd('set termguicolors tgc')
-- vim.cmd('colorscheme solarized-high')
vim.cmd[[colorscheme tokyonight]]

if vim.fn.has('gui_running') == 0 then
    vim.g.solarized_termtrans = 0
else
    vim.g.solarized_termtrans = 1
end

