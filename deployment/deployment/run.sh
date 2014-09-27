#!/bin/bash

usage()
{
    echo "Usage: ${0##*/} {setup|update} {local|remote}"
    exit 1
}

setup_env()
{
case "$ENV" in
    local)
        ansible-playbook -i inventory/local.ini --private-key=~/.vagrant.d/insecure_private_key -u vagrant wordie-setup.yaml
        ;;
    remote)
        ansible-playbook -i inventory/remote.ini -u root wordie-setup.yaml
        ;;
    *)
        usage
        ;;
esac
}

update_env()
{
case "$ENV" in
    local)
        ansible-playbook -i inventory/local.ini --private-key=~/.vagrant.d/insecure_private_key -u vagrant wordie-update.yaml
        ;;
    remote)
        ansible-playbook -i inventory/remote.ini -u root wordie-update.yaml
        ;;
    *)
        usage
        ;;
esac
}

ACTION=$1
ENV=$2

case "$ACTION" in

    setup)
        setup_env
        ;;
    update)
        update_env
        ;;
    *)
        usage
        ;;
esac

exit 0


