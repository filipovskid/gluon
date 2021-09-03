from executor import ExecutorApp
import argparse


def parse_arguments():
    parser = argparse.ArgumentParser(description='Start the executor interactive shell and listen for incoming ' +
                                                 'requests at a given port')
    parser.add_argument('--port', type=int, required=True,
                        help='port on which the gRPC server will listen for execution requests')

    return parser.parse_args()


if __name__ == '__main__':
    args = parse_arguments()

    executor = ExecutorApp(port=args.port)
    executor.start()
