HashMap  Put方法
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    //如果table为空或者长度为0，则resize()
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    //确定插入table的位置，算法是(n - 1) & hash，在n为2的幂时，相当于取摸操作。
    ////找到key值对应的槽并且是第一个，直接加入
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    //在table的i位置发生冲突，有两种情况，1、key值是一样的，替换value值，
    //2、key值不一样的有两种处理方式：2.1、存储在i位置的链表；2.2、存储在红黑树中
    else {
        Node<K,V> e; K k;
        //第一个node的hash值即为要加入元素的hash
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        //2.2
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        //2.1
        else {
            //不是TreeNode,即为链表,遍历链表
            for (int binCount = 0; ; ++binCount) {
            ///链表的尾端也没有找到key值相同的节点，则生成一个新的Node,
            //并且判断链表的节点个数是不是到达转换成红黑树的上界达到，则转换成红黑树。
                if ((e = p.next) == null) {
                     // 创建链表节点并插入尾部
                    p.next = newNode(hash, key, value, null);
                    ////超过了链表的设置长度8就转换成红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        //如果e不为空就替换旧的oldValue值
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}

HashMap Get方法
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
		//table数组中根据hash值找到对应的Node则返回
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
			//如果对应的是红黑树，则根据hash和key值在红黑树中查找
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
				//不是红黑树则对应的是链表Node，根据hash和key值遍历链表查找
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
