import os
import sys
import time
import socket
import tensorflow as tf
import numpy as np
import pandas as pd

def define_graph():
    global usr_indx, itm_indx, rates_tbl, result,base_cost, cost, training_step

    usr_indx = tf.placeholder(dtype=tf.int32)
    itm_indx = tf.placeholder(dtype=tf.int32)
    rates_tbl = tf.placeholder(dtype=tf.int32)
    # In matrix factorization user rating R is formulated as an inner product of two latent vectors U  and U  which are two
    # latent vectors in same space to represent the user interest and movie feature respectively.
    # Dataset contains of 100,000 ratings (1-5) from 943 users on 1682 movies
    feature_len = 10
    # tf.reset_default_graph()
    U = tf.Variable(initial_value=tf.truncated_normal([943, feature_len]), name='users')
    P = tf.Variable(initial_value=tf.truncated_normal([feature_len, 1682]), name='items')

    # The prediction matrix is the multiplication of these two matrices
    result = tf.matmul(U, P)
    result_flatten = tf.reshape(result, [-1])

    # Gather slices from 'params=result_flatten' axis 'axis' according to 'indices'
    # indices is teken from flattened_matrix using index(2D array like)  -- userIndex * total_movies + itemIndex ;
    # we lookup (gather) the indices that we have the training data for
    R = tf.gather(result_flatten, usr_indx * tf.shape(result)[1] +
                  itm_indx, name='extracting_user_rate')

    # Cost function
    diff_op_squared = tf.square(tf.subtract(R, tf.cast(rates_tbl, tf.float32)), name="squared_difference")
    base_cost = tf.reduce_sum(diff_op_squared, name="sum_squared_error")

    # Regularization
    lda = 0.001
    norm_sums = tf.add(tf.reduce_sum(tf.abs(U), name='user_norm'), tf.reduce_sum(tf.abs(P), name='item_norm'))
    regularizer = lda * norm_sums

    cost = tf.add(base_cost, regularizer)

    global_step = tf.Variable(0, trainable=False)
    # lr = 0.001
    # learning_rate = tf.train.exponential_decay(lr, global_step, 10000, 0.96, staircase=True)
    # optimizer = tf.train.GradientDescentOptimizer(learning_rate)
    optimizer = tf.train.AdamOptimizer(learning_rate=0.01)
    training_step = optimizer.minimize(cost, global_step=global_step)
    pass


def load_dataset():
    global df, df_test, df_train, user_indecies, item_indecies, user_indecies_test, item_indecies_test, rates, rates_test
    df = pd.read_csv('./ml-100k/u.data', sep='\t', names=['user', 'item', 'rate', 'time'])

    # SHIFTING INDICES FROM 1...N TO 0...N-1
    df['user'] = df['user'].values - 1
    df['item'] = df['item'].values - 1
    # CRAEATE MASKING FOR REMOVING 30% OF DATA
    msk = np.random.rand(len(df)) < 0.7
    df_train = df[msk]
    # STORING INDICES
    user_indecies = [x for x in df_train.user.values]
    item_indecies = [x for x in df_train.item.values]
    # THE RATINGS
    rates = df_train.rate.values

    # FOR TEST DATA
    df_test = df[~msk]
    user_indecies_test = [x for x in df_test.user.values]
    item_indecies_test = [x for x in df_test.item.values]
    rates_test = df_test.rate.values
    pass



def train_recommender():
    global usr_indx, itm_indx, rates_tbl, result, base_cost, cost, training_step, sess # from define_graph()
    global df, df_test, df_train, user_indecies, item_indecies,\
        user_indecies_test, item_indecies_test, rates, rates_test   # from load_dataset()

    init = tf.global_variables_initializer()
    sess.run(init)

    train_dict = {usr_indx:user_indecies_test, itm_indx:item_indecies_test, rates_tbl:rates_test}
    test_dict = {usr_indx:user_indecies, itm_indx:item_indecies, rates_tbl:rates}

    for i in range(401):  # optimal is 400
        sess.run(training_step, feed_dict={usr_indx:user_indecies, itm_indx:item_indecies, rates_tbl:rates})
        if i % 50 == 0:
            test_cst = sess.run(base_cost, feed_dict=train_dict)/df_test.shape[0]
            train_cst = sess.run(base_cost, feed_dict=test_dict)/df_train.shape[0]
            print('step=', i, 'error=', test_cst)
            print('step=', i, 'error=', train_cst)

    for i in range(10):
        ii = np.random.randint(0, df.shape[0])
        u, p, r = df[['user', 'item', 'rate']].values[ii]
        rhat = tf.gather(tf.gather(result, u), p)
        print("rating for user ", u, " for item ", p,
              " is ", r, " and our prediction is: ", sess.run(rhat))

    final = sess.run(cost, feed_dict=test_dict)/df_train.shape[0]
    # print('final test error=',final)
    return final


def get_top(user, top=10):
    global sess, result
    rhat = tf.gather(result, user)
    results = sess.run(rhat)
    # print("user rating prediction is: \n", results)
    indx = np.argsort(results)
    indx = indx[-top:][::-1]  # taking the last 10 and reversing
    # print(np.shape(indx), indx)
    print("top rating prediction is: \n", results[indx])
    print("of movies: \n", indx)
    return indx.tolist()


if __name__ == '__main__':

    # sess.close()
    tf.reset_default_graph()
    define_graph()

    saver = tf.train.Saver()
    sess = tf.Session()
    # saver.restore(sess, './saved_session.ckpt')

    host = ''
    port = 8080
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    print('Socket connected')
    sock.bind((host, port))
    sock.listen(100)

    try:    # this block for catching ctrl+c and closing tf.sess, socket
        while True: # run always , accept clients,
            client, address = sock.accept()
            print("Got a connection from %s" % str(address))  # successfully connected
            msg = 'Hello From Python'
            client.send(msg.encode())

            #listen until the client is closed
            while not client._closed:
                msg = client.recv(1024)

                if not msg: break
                if len(msg) < 3: continue

                #multiple messages can come concatinated so breaking
                msgs = msg.split(b'\n')
                for msg in msgs:
                    msg = msg.decode().strip()  #msg is byte, so decoding

                    print(msg)
                    if msg == 'end':
                        client.send('closing connection from Python'.encode())
                        client.close()
                        break
                    elif msg == 'train_recommender':
                        load_dataset()
                        err = train_recommender()
                        client.send(('cost = '+str(err)).encode())
                        saver.save(sess, './saved_session.ckpt')
                    elif msg.startswith('get_top'):
                        splits = msg.split(',')
                        # print(splits)
                        tops = get_top(int(splits[1]), int(splits[2]))
                        # print(tops)
                        str1 = ''.join(str(e)+' ,' for e in tops)
                        str1= 'top = ['+str1[:-1]+']'
                        client.send(str1.encode())


    except KeyboardInterrupt:
        pass
    finally:
        sess.close()
        sock.close()
